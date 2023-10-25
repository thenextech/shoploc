package nextech.shoploc.services.merchantSchedule;

import nextech.shoploc.domains.MerchantSchedule;
import nextech.shoploc.models.merchantSchedule.MerchantScheduleRequestDTO;
import nextech.shoploc.models.merchantSchedule.MerchantScheduleResponseDTO;
import nextech.shoploc.repositories.MerchantScheduleRepository;
import nextech.shoploc.utils.ModelMapperUtils;
import nextech.shoploc.utils.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MerchantScheduleServiceImpl implements MerchantScheduleService {
    private final MerchantScheduleRepository merchantScheduleRepository;
    private final ModelMapperUtils modelMapperUtils;

    public MerchantScheduleServiceImpl(MerchantScheduleRepository merchantScheduleRepository, ModelMapperUtils modelMapperUtils) {
        this.merchantScheduleRepository = merchantScheduleRepository;
        this.modelMapperUtils = modelMapperUtils;
    }

    @Override
    public MerchantScheduleResponseDTO createMerchantSchedule(MerchantScheduleRequestDTO merchantScheduleRequestDTO) {
        MerchantSchedule merchantSchedule = modelMapperUtils.getModelMapper().map(merchantScheduleRequestDTO, MerchantSchedule.class);
        merchantSchedule = merchantScheduleRepository.save(merchantSchedule);
        return modelMapperUtils.getModelMapper().map(merchantSchedule, MerchantScheduleResponseDTO.class);
    }

    @Override
    public MerchantScheduleResponseDTO getMerchantScheduleById(Long id) {
        MerchantSchedule merchantSchedule = merchantScheduleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("MerchantSchedule not found with ID: " + id));
        return modelMapperUtils.getModelMapper().map(merchantSchedule, MerchantScheduleResponseDTO.class);
    }

    @Override
    public List<MerchantScheduleResponseDTO> getAllMerchantSchedules() {
        List<MerchantSchedule> merchantSchedules = merchantScheduleRepository.findAll();
        return merchantSchedules.stream()
                .map(merchantSchedule -> modelMapperUtils.getModelMapper().map(merchantSchedule, MerchantScheduleResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteMerchantSchedule(Long id) {
        if (!merchantScheduleRepository.existsById(id)) {
            throw new NotFoundException("MerchantSchedule not found with ID: " + id);
        }
        merchantScheduleRepository.deleteById(id);
    }

    @Override
    public MerchantScheduleResponseDTO updateMerchantSchedule(Long id, MerchantScheduleRequestDTO merchantScheduleRequestDTO) {
        MerchantSchedule merchantSchedule = merchantScheduleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("MerchantSchedule not found with ID: " + id));
        modelMapperUtils.getModelMapper().map(merchantScheduleRequestDTO, merchantSchedule);
        merchantSchedule = merchantScheduleRepository.save(merchantSchedule);
        return modelMapperUtils.getModelMapper().map(merchantSchedule, MerchantScheduleResponseDTO.class);
    }
}
