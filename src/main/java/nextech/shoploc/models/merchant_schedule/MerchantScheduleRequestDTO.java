package nextech.shoploc.models.merchant_schedule;

import lombok.Getter;
import lombok.Setter;
import nextech.shoploc.domains.enums.DayOfWeek;

@Getter
@Setter
public class MerchantScheduleRequestDTO {
    private DayOfWeek dayOfWeek;
    private String openingTime;
    private String closingTime;
}