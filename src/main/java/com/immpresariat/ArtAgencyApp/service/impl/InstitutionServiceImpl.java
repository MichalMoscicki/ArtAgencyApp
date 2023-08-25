package com.immpresariat.ArtAgencyApp.service.impl;

import com.immpresariat.ArtAgencyApp.exception.ResourceAlreadyExistsException;
import com.immpresariat.ArtAgencyApp.exception.ResourceNotFoundException;
import com.immpresariat.ArtAgencyApp.models.Institution;
import com.immpresariat.ArtAgencyApp.payload.InstitutionDTO;
import com.immpresariat.ArtAgencyApp.repository.InstitutionRepository;
import com.immpresariat.ArtAgencyApp.service.InstitutionService;
import com.immpresariat.ArtAgencyApp.utils.DTOMapper;
import com.immpresariat.ArtAgencyApp.utils.InputCleaner;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class    InstitutionServiceImpl implements InstitutionService {

    final private InstitutionRepository institutionRepository;
    final private InputCleaner inputCleaner;
    final private DTOMapper dtoMapper;

    public InstitutionServiceImpl(InstitutionRepository institutionRepository,
                                  InputCleaner inputCleaner,
                                  DTOMapper dtoMapper) {
        this.institutionRepository = institutionRepository;
        this.inputCleaner = inputCleaner;
        this.dtoMapper = dtoMapper;
    }

    @Override
    public InstitutionDTO create(InstitutionDTO unsynchronizedInstitutionDTO) {

        Optional<Institution> institutionOptional = institutionRepository.
                findInstitutionByNameAndCity(unsynchronizedInstitutionDTO.getName(), unsynchronizedInstitutionDTO.getCity());

        if (institutionOptional.isPresent()) {
            throw new ResourceAlreadyExistsException(String.format("Institution with given name: %s and city: %s already exists",
                    unsynchronizedInstitutionDTO.getName(), unsynchronizedInstitutionDTO.getCity()));
        }

        Institution unsynchronizedInstitution = dtoMapper.mapUnsyncDTOToInstitution(unsynchronizedInstitutionDTO);
        Institution synchronizedInstitution = institutionRepository.save(inputCleaner.clean(unsynchronizedInstitution));

        return dtoMapper.mapInstitutionToDTO(synchronizedInstitution);
    }

    @Override
    public List<InstitutionDTO> getAll() {
        List<Institution> institutions = institutionRepository.findAll();

        return institutions.stream()
                .map(dtoMapper::mapInstitutionToDTO).collect(Collectors.toList());
    }

    @Override
    public InstitutionDTO update(InstitutionDTO updatedInstitutionDTO) {
        Long id = updatedInstitutionDTO.getId();
        Optional<Institution> institutionOptional = institutionRepository.findById(id);

        if (institutionOptional.isEmpty()) {
            throw new ResourceNotFoundException(String.format("No institution with id: %s", id));
        } else {
            Institution updatedInstitution = dtoMapper.mapDTOToInstitution(updatedInstitutionDTO);
            Institution institutionDB = institutionRepository.save(inputCleaner.clean(updatedInstitution));
            return dtoMapper.mapInstitutionToDTO(institutionDB);
        }
    }

    @Override
    public InstitutionDTO getById(Long id) {
        Optional<Institution> institutionOptional = institutionRepository.findById(id);

        if (institutionOptional.isEmpty()) {
            throw new ResourceNotFoundException(String.format("No institution with id: %s", id));
        }

        return dtoMapper.mapInstitutionToDTO(institutionOptional.get());
    }

    @Override
    public void deleteWithAssociatedData(Long id) {
        institutionRepository.deleteById(id);

    }


}
