package nextech.shoploc.services.merchantSchedule;

import nextech.shoploc.models.merchantSchedule.MerchantScheduleRequestDTO;
import nextech.shoploc.models.merchantSchedule.MerchantScheduleResponseDTO;

import java.util.List;

public interface MerchantScheduleService {
    MerchantScheduleResponseDTO createMerchantSchedule(MerchantScheduleRequestDTO merchantScheduleRequestDTO);

    MerchantScheduleResponseDTO getMerchantScheduleById(Long id);

    List<MerchantScheduleResponseDTO> getAllMerchantSchedules();

    void deleteMerchantSchedule(Long id);

    MerchantScheduleResponseDTO updateMerchantSchedule(Long id, MerchantScheduleRequestDTO merchantScheduleRequestDTO);
}
