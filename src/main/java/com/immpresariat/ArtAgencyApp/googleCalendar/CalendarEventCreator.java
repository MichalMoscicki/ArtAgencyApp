package com.immpresariat.ArtAgencyApp.googleCalendar;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.immpresariat.ArtAgencyApp.exception.ResourceNotFoundException;
import com.immpresariat.ArtAgencyApp.models.Concert;
import com.immpresariat.ArtAgencyApp.repository.ConcertRepository;
import org.springframework.stereotype.Component;
import com.google.api.services.calendar.model.Event;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class CalendarEventCreator {

    private final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    private final Calendar service =
            new Calendar.Builder(HTTP_TRANSPORT, GoogleCalendarConnector.JSON_FACTORY, GoogleCalendarConnector.getCredentials(HTTP_TRANSPORT))
                    .setApplicationName(GoogleCalendarConnector.APPLICATION_NAME)
                    .build();

    private final ConcertRepository concertRepository;

    public CalendarEventCreator(ConcertRepository concertRepository) throws GeneralSecurityException, IOException {
        this.concertRepository = concertRepository;
    }

    public Event createEvent(Long concertId) throws IOException {
        Concert concert = ensureConcertExists(concertId);

        Event event = new Event()
                .setSummary(concert.getTitle())
                .setLocation(concert.getAddress())
                .setDescription(getDescriptionWithSongs(concert));


        EventDateTime start = new EventDateTime()
                .setDateTime(new DateTime(concert.getStart().toString() + ":00"))
                .setTimeZone("Europe/Warsaw");
        event.setStart(start);

        EventDateTime end = new EventDateTime()
                .setDateTime(new DateTime(concert.getEnd().toString()  + ":00"))
                .setTimeZone("Europe/Warsaw");
        event.setEnd(end);

        event.setAttendees(addAttendingMusicians(concert));

        Event.Reminders reminders = new Event.Reminders()
                .setUseDefault(true);
        event.setReminders(reminders);

        event = service.events().insert("primary", event).execute();

        return event;
    }

    private  List<EventAttendee> addAttendingMusicians(Concert concert) {
        return concert.getMusicians().stream()
                .map(musician -> new EventAttendee().setEmail(musician.getEmail())).collect(Collectors.toList());
    }

    private String getDescriptionWithSongs(Concert concert) {
        String descriptionWithSongs = concert.getDescription();
        StringBuilder builder = new StringBuilder();
        builder.append("\n").append("\n").append("Setlista: \n");
        IntStream.range(0, concert.getSongs().size())
                .mapToObj(i -> i + 1 + " " + concert.getSongs().get(i).getTitle() + "\n")
                .forEach(builder::append);

        return descriptionWithSongs.concat(builder.toString());
    }

    private Concert ensureConcertExists(Long concertId) {
        return concertRepository.findById(concertId)
                .orElseThrow(() -> new ResourceNotFoundException("No concert with id: " + concertId));
    }

}
