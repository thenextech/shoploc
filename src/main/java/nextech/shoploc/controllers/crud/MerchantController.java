package nextech.shoploc.controllers.crud;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import nextech.shoploc.models.merchant.MerchantRequestDTO;
import nextech.shoploc.models.merchant.MerchantResponseDTO;
import nextech.shoploc.repositories.OrderRepository;
import nextech.shoploc.services.merchant.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/merchants")
@Api(tags = "Merchants")
public class MerchantController {

    private final MerchantService merchantService;
    private final OrderRepository orderRepository;
    @Autowired
    public MerchantController(final MerchantService merchantService, OrderRepository orderRepository) {
        this.merchantService = merchantService;
        this.orderRepository = orderRepository;
    }

    @PostMapping("/create")
    @ApiOperation(value = "Create a merchant", notes = "Creates a new merchant")
    public ResponseEntity<MerchantResponseDTO> createMerchant(@RequestBody MerchantRequestDTO merchantRequestDTO) {
        MerchantResponseDTO createdMerchant = merchantService.createMerchant(merchantRequestDTO);
        return new ResponseEntity<>(createdMerchant, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get merchant by ID", notes = "Retrieve a merchant by their ID")
    public ResponseEntity<MerchantResponseDTO> getMerchantById(@PathVariable Long id) {
        MerchantResponseDTO merchant = merchantService.getMerchantById(id);
        if (merchant != null) {
            return new ResponseEntity<>(merchant, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/email/{email}")
    @ApiOperation(value = "Get merchant by email", notes = "Retrieve a merchant by their email address")
    public ResponseEntity<MerchantResponseDTO> getMerchantByEmail(@PathVariable String email) {
        MerchantResponseDTO merchant = merchantService.getMerchantByEmail(email);
        if (merchant != null) {
            return new ResponseEntity<>(merchant, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all")
    @Operation(summary = "Get all Merchants")
    public ResponseEntity<List<MerchantResponseDTO>> getAllMerchants() {
        List<MerchantResponseDTO> merchants = merchantService.getAllMerchants();
        return new ResponseEntity<>(merchants, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Update merchant", notes = "Update an existing merchant by their ID")
    public ResponseEntity<MerchantResponseDTO> updateMerchant(@PathVariable Long id, @RequestBody MerchantRequestDTO merchantRequestDTO) {
        Optional<MerchantResponseDTO> merchantResponseDTO = Optional.ofNullable(merchantService.updateMerchant(id, merchantRequestDTO));
        return merchantResponseDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete a merchant", notes = "Delete a merchant by their ID")
    public ResponseEntity<Void> deleteMerchant(@PathVariable Long id) {
        merchantService.deleteMerchant(id);
        return ResponseEntity.noContent().build();
    }





    @GetMapping("/stats")
    @ApiOperation(value = "Get sales statistics", notes = "Retrieve sales statistics for a merchant")
    public ResponseEntity<Map<String, Object>> getSalesStatistics(
            @RequestParam Long merchantId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {

        LocalDateTime startDateTime = parseDateTime(startDate, LocalDateTime.now().with(LocalTime.MIN));
        LocalDateTime endDateTime = parseDateTime(endDate, LocalDateTime.now().with(LocalTime.MAX));

        LocalDateTime startOfWeek = getStartOfWeek(LocalDateTime.now());
        LocalDateTime endOfWeek = getEndOfWeek(LocalDateTime.now());

        LocalDateTime startOfMonth = getStartOfMonth(startDateTime);

        LocalDateTime endOfMonth = getEndOfMonth(endDateTime);

        LocalDateTime startOfYear = getStartOfYear(LocalDateTime.now());
        LocalDateTime endOfYear = getEndOfYear(endDateTime);

        Map<String, Object> statisticsMap = new HashMap<>();

        // Chiffre d'affaires total
        statisticsMap.put("nbrAllOrders", orderRepository.findOrdersByMerchantId(merchantId).size());
        statisticsMap.put("nbrOrdersBetweenTwoDates", orderRepository.findOrdersBetweenDatesByMerchantId(merchantId, startDateTime, endDateTime).size());
        statisticsMap.put("totalRevenue", orderRepository.calculateTotalRevenue(merchantId, startDateTime, endDateTime));

        // Chiffre d'affaires quotidien pendant la période spécifiée
        Double dailyRevenue = orderRepository.calculateDailyRevenue(merchantId,
                LocalDateTime.now().withHour(0).withMinute(1).withSecond(0).withNano(0),
                LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(999999999));
        Double dailyRevenueOfYesterday = orderRepository.calculateDailyRevenue(merchantId,
                LocalDateTime.now().minusDays(1).withHour(0).withMinute(1).withSecond(0).withNano(0),
                LocalDateTime.now().minusDays(1).withHour(23).withMinute(59).withSecond(59).withNano(999999999));

        statisticsMap.put("dailyRevenueOfYesterday", dailyRevenueOfYesterday);


        statisticsMap.put("dailyRevenue", dailyRevenue);
        // Chiffre annuel
        statisticsMap.put("yearlyRevenue", orderRepository.calculateYearlyRevenue(merchantId, startDateTime, endDateTime));

        // Evolution du chiffre de la semaine (à compléter)

        // Catégories et produits les plus vendus
        statisticsMap.put("topCategories", orderRepository.findTopCategories(merchantId, startDateTime, endDateTime));
        statisticsMap.put("topProducts", orderRepository.findTopProducts(merchantId, startDateTime, endDateTime));
        statisticsMap.put("topCategoriesWithRevenue", orderRepository.findTopCategoriesWithRevenue(merchantId, startDateTime, endDateTime));
        statisticsMap.put("topProductsWithRevenue", orderRepository.findTopProductsWithRevenue(merchantId, startDateTime, endDateTime));

        // Nombre de clients utilisant le système Shoploc par semaine, mois, année
        statisticsMap.put("nbrClientsThisWeek", orderRepository.nombreClientsToMerchantId(merchantId, startOfWeek, endOfWeek));
        statisticsMap.put("nbrClientsThisMonth", orderRepository.nombreClientsToMerchantId(merchantId, startOfMonth, endOfMonth));
        statisticsMap.put("nbrClientsThisYear", orderRepository.nombreClientsToMerchantId(merchantId, startOfYear, endOfYear));

        // Montant des achats réalisés par Shoploc par semaine, mois, année
        statisticsMap.put("totalRevenueThisWeek", orderRepository.calculateTotalRevenue(merchantId, startOfWeek, endOfWeek));
        statisticsMap.put("totalRevenueThisMonth", orderRepository.calculateTotalRevenue(merchantId, startOfMonth, endOfMonth));
        statisticsMap.put("totalRevenueThisYear", orderRepository.calculateTotalRevenue(merchantId, startOfYear, endOfYear));

        // Classement des produits les plus consultés et les plus achetés
        statisticsMap.put("mostViewedProducts", orderRepository.findMostViewedProducts(merchantId, startDateTime, endDateTime));
        statisticsMap.put("mostPurchasedProducts", orderRepository.findMostPurchasedProducts(merchantId, startDateTime, endDateTime));

        return ResponseEntity.ok(statisticsMap);
    }

    private LocalDateTime parseDateTime(String dateTimeString, LocalDateTime defaultValue) {
        if (dateTimeString == null || dateTimeString.isEmpty()) {
            return defaultValue;
        }
        return LocalDateTime.parse(dateTimeString, DateTimeFormatter.ISO_DATE_TIME);
    }

    private LocalDateTime getStartOfWeek(LocalDateTime dateTime) {
        return dateTime.equals(dateTime.toLocalDate().atStartOfDay()) ? dateTime.toLocalDate().with(DayOfWeek.MONDAY).atStartOfDay() : dateTime;
    }

    private LocalDateTime getEndOfWeek(LocalDateTime dateTime) {
        return dateTime.equals(dateTime.toLocalDate().atStartOfDay()) ? dateTime.toLocalDate().with(DayOfWeek.SUNDAY).atTime(LocalTime.MAX) : dateTime;
    }

    private LocalDateTime getStartOfMonth(LocalDateTime dateTime) {
        return dateTime.equals(dateTime.toLocalDate().withDayOfMonth(1).atStartOfDay()) ? dateTime : dateTime.toLocalDate().withDayOfMonth(1).atStartOfDay();
    }

    private LocalDateTime getEndOfMonth(LocalDateTime dateTime) {
        return dateTime.equals(dateTime.toLocalDate().withDayOfMonth(1).atStartOfDay()) ? dateTime.toLocalDate().withDayOfMonth(dateTime.toLocalDate().lengthOfMonth()).atTime(LocalTime.MAX) : dateTime;
    }

    private LocalDateTime getStartOfYear(LocalDateTime dateTime) {
        return dateTime.equals(dateTime.toLocalDate().withDayOfYear(1).atStartOfDay()) ? dateTime : dateTime.toLocalDate().withDayOfYear(1).atStartOfDay();
    }

    private LocalDateTime getEndOfYear(LocalDateTime dateTime) {
        return dateTime.equals(dateTime.toLocalDate().withDayOfYear(1).atStartOfDay()) ? dateTime.toLocalDate().withDayOfYear(dateTime.toLocalDate().lengthOfYear()).atTime(LocalTime.MAX) : dateTime;
    }



}
