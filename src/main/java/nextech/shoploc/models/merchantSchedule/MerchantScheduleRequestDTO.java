package nextech.shoploc.models.merchantSchedule;

import nextech.shoploc.domains.enums.DayOfWeek;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MerchantScheduleRequestDTO {
    private DayOfWeek dayOfWeek;
    private String openingTime;
    private String closingTime;
}