package nextech.shoploc.services.merchant;

import nextech.shoploc.domains.Merchant;
import nextech.shoploc.domains.Order;
import nextech.shoploc.domains.enums.Status;
import nextech.shoploc.models.merchant.MerchantRequestDTO;
import nextech.shoploc.models.merchant.MerchantResponseDTO;
import nextech.shoploc.repositories.MerchantRepository;
import nextech.shoploc.repositories.OrderRepository;
import nextech.shoploc.services.auth.EmailSenderService;
import nextech.shoploc.utils.ModelMapperUtils;
import nextech.shoploc.utils.exceptions.EmailAlreadyExistsException;
import nextech.shoploc.utils.exceptions.MerchantNotFoundException;
import nextech.shoploc.utils.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MerchantServiceImpl implements MerchantService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final MerchantRepository merchantRepository;
    private final OrderRepository orderRepository;


    private final ModelMapperUtils modelMapperUtils;
    
    @Autowired
    private EmailSenderService emailSenderService;

    public MerchantServiceImpl(MerchantRepository merchantRepository, OrderRepository orderRepository, ModelMapperUtils modelMapperUtils) {
        this.merchantRepository = merchantRepository;
        this.orderRepository = orderRepository;
        this.modelMapperUtils = modelMapperUtils;
    }

    @Override
    public MerchantResponseDTO createMerchant(MerchantRequestDTO merchantRequestDTO) throws EmailAlreadyExistsException {
        try {
        	Merchant merchant = modelMapperUtils.getModelMapper().map(merchantRequestDTO, Merchant.class);
            String encodedPassword = passwordEncoder.encode(merchant.getPassword());
            merchant.setPassword(encodedPassword);
            merchant = merchantRepository.save(merchant);
            return modelMapperUtils.getModelMapper().map(merchant, MerchantResponseDTO.class);
        } catch(Exception e) {
        	throw new EmailAlreadyExistsException();
        }
    }

    @Override
    public MerchantResponseDTO getMerchantById(Long id) {
        Merchant merchant = merchantRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Merchant not found with ID: " + id));
        return modelMapperUtils.getModelMapper().map(merchant, MerchantResponseDTO.class);
    }

    @Override
    public MerchantResponseDTO getMerchantByEmail(String email) {
        Merchant merchant = merchantRepository.findMerchantByEmail(email)
                .orElseThrow(() -> new NotFoundException("Merchant not found with email: " + email));
        return modelMapperUtils.getModelMapper().map(merchant, MerchantResponseDTO.class);
    }

    @Override
    public List<MerchantResponseDTO> getAllMerchants() {
        List<Merchant> merchants = merchantRepository.findAll();
        return merchants.stream()
                .map(merchant -> modelMapperUtils.getModelMapper().map(merchant, MerchantResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteMerchant(Long id) {
        if (!merchantRepository.existsById(id)) {
            throw new NotFoundException("Merchant not found with ID: " + id);
        }
        merchantRepository.deleteById(id);
    }

    @Override
    public MerchantResponseDTO updateMerchant(Long id, MerchantRequestDTO merchantRequestDTO) {
        Merchant merchant = merchantRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Merchant not found with ID: " + id));

        modelMapperUtils.getModelMapper().map(merchantRequestDTO, merchant);
        merchant = merchantRepository.save(merchant);
        return modelMapperUtils.getModelMapper().map(merchant, MerchantResponseDTO.class);
    }
    
    @Override
    public void activateMerchant(Long merchantId) {
    	try {
    		Optional<Merchant> optionalMerchant = merchantRepository.findMerchantByUserId(merchantId);
            if (optionalMerchant.isPresent()) {
                Merchant merchant = optionalMerchant.get();
                merchant.setStatus(Status.ACTIVE);
                //emailSenderService.sendMerchantAccountActivatedEmail(merchant.getEmail());
                merchantRepository.save(merchant); 
            } else {
                throw new MerchantNotFoundException("Commerçant non trouvé avec l'identifiant : " + merchantId);
            }   		
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    }

    @Override
    public Map<String, Object> getSalesStatistics(Long merchantId, String startDate, String endDate) {
        LocalDateTime startDateTime = parseDateTime(startDate, LocalDateTime.now().with(LocalTime.MIN));
        LocalDateTime endDateTime = parseDateTime(endDate, LocalDateTime.now().with(LocalTime.MAX));

        Map<String, Object> statisticsMap = new HashMap<>();

        // Nombre total de commandes et de clients distincts sur la période spécifiée
        List<Order> ordersBetweenDates = orderRepository.findOrdersBetweenDatesByMerchantId(merchantId, startDateTime, endDateTime);
        statisticsMap.put("nbrOrdersBetweenTwoDates", ordersBetweenDates.size());

        Integer distinctClients = orderRepository.nombreClientsToMerchantId(merchantId, startDateTime, endDateTime);
        statisticsMap.put("nbrDistinctClients", distinctClients);

        Integer nombreTotalClients = orderRepository.nombreTotalClients(startDateTime, endDateTime);
        statisticsMap.put("nombreTotalClients", nombreTotalClients);

        // Chiffre d'affaires total pour un marchand dans une plage de dates
        Double totalRevenue = orderRepository.calculateTotalRevenue(merchantId, startDateTime, endDateTime);
        statisticsMap.put("totalRevenue", totalRevenue);

        // Chiffre d'affaires total pour un marchand dans une plage de dates
        Double totalRevenueComplet = orderRepository.calculateTotalRevenueComplet(merchantId);
        statisticsMap.put("totalRevenueComplet", totalRevenueComplet);

        // Chiffre d'affaires total pour un marchand dans une plage de dates
        Double todayRevenue = orderRepository.calculateTodayRevenue(merchantId, LocalDateTime.now().with(LocalTime.MIN),LocalDateTime.now().with(LocalTime.MAX));
        statisticsMap.put("todayRevenue", todayRevenue);

        // Début de la semaine courante et passée
        LocalDateTime startOfThisWeek = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).atStartOfDay();
        LocalDateTime startOfLastWeek = startOfThisWeek.minusWeeks(1);

        // Calculer et stocker le revenu de la semaine courante
        statisticsMap.put("weekRevenue", orderRepository.calculateTodayRevenue(merchantId, startOfThisWeek, LocalDateTime.now()));

        // Calculer et stocker le revenu de la semaine passée
        statisticsMap.put("lastWeekRevenue", orderRepository.calculateTodayRevenue(merchantId, startOfLastWeek, startOfThisWeek.minusSeconds(1)));


        // Calcul du revenu de l'année en cours
        LocalDate startOfCurrentYear = LocalDate.now().withDayOfYear(1);
        LocalDate endOfCurrentYear = startOfCurrentYear.with(TemporalAdjusters.lastDayOfYear());
        Double currentYearRevenue = orderRepository.calculateTodayRevenue(merchantId, startOfCurrentYear.atStartOfDay(), endOfCurrentYear.atTime(LocalTime.MAX));
        statisticsMap.put("yearRevenue", currentYearRevenue);

        // Calcul du revenu de l'année précédente
        LocalDate startOfLastYear = LocalDate.now().minusYears(1).withDayOfYear(1);
        LocalDate endOfLastYear = startOfLastYear.with(TemporalAdjusters.lastDayOfYear());
        Double lastYearRevenue = orderRepository.calculateTodayRevenue(merchantId, startOfLastYear.atStartOfDay(), endOfLastYear.atTime(LocalTime.MAX));
        statisticsMap.put("lastYearRevenue", lastYearRevenue);

        // Calcul du revenu du mois en cours
        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate endOfMonth = startOfMonth.with(TemporalAdjusters.lastDayOfMonth()); // Ajusté pour la fin du mois
        Double monthRevenue = orderRepository.calculateTodayRevenue(merchantId, startOfMonth.atStartOfDay(), endOfMonth.atTime(LocalTime.MAX));
        statisticsMap.put("monthRevenue", monthRevenue);

        // Calcul du revenu du mois précédent
        LocalDate startOfLastMonth = LocalDate.now().minusMonths(1).withDayOfMonth(1);
        LocalDate endOfLastMonth = startOfLastMonth.with(TemporalAdjusters.lastDayOfMonth()); // Obtient le dernier jour du mois précédent
        Double lastMonthRevenue = orderRepository.calculateTodayRevenue(merchantId, startOfLastMonth.atStartOfDay(), endOfLastMonth.atTime(LocalTime.MAX));
        statisticsMap.put("lastMonthRevenue", lastMonthRevenue);


        Double yesterdayRevenue = orderRepository.calculateTodayRevenue(merchantId, LocalDateTime.now().minusDays(1).with(LocalTime.MIN),LocalDateTime.now().minusDays(1).with(LocalTime.MAX));
        statisticsMap.put("yesterdayRevenue", yesterdayRevenue);
        // Chiffre d'affaires total pour un marchand dans une plage de dates
        List<Object[]> revenuePerDaysBetweenDates = orderRepository.calculateRevenuePerDaysBetweenDates(merchantId, startDateTime,endDateTime);
        statisticsMap.put("revenuePerDaysBetweenDates", revenuePerDaysBetweenDates);

        // Catégories et produits les plus vendus avec leur revenu
        List<String> topCategories = orderRepository.findTopCategories(merchantId, startDateTime, endDateTime);
        List<String> topProducts = orderRepository.findTopProducts(merchantId, startDateTime, endDateTime);
        List<Map<String, Double>> topCategoriesWithRevenue = orderRepository.findTopCategoriesWithRevenue(merchantId, startDateTime, endDateTime);
        List<Map<String, Double>> topProductsWithRevenue = orderRepository.findTopProductsWithRevenue(merchantId, startDateTime, endDateTime);

        statisticsMap.put("topCategories", topCategories);
        statisticsMap.put("topProducts", topProducts);
        statisticsMap.put("topCategoriesWithRevenue", topCategoriesWithRevenue);
        statisticsMap.put("topProductsWithRevenue", topProductsWithRevenue);

        // Classement des produits les plus consultés et les plus achetés
        List<String> mostViewedProducts = orderRepository.findMostViewedProducts(merchantId, startDateTime, endDateTime);
        List<String> mostPurchasedProducts = orderRepository.findMostPurchasedProducts(merchantId, startDateTime, endDateTime);

        statisticsMap.put("mostViewedProducts", mostViewedProducts);
        statisticsMap.put("mostPurchasedProducts", mostPurchasedProducts);

        return statisticsMap;
    }

    private LocalDateTime parseDateTime(String dateTimeString, LocalDateTime defaultValue) {
        if (dateTimeString == null || dateTimeString.isEmpty()) {
            return defaultValue;
        }
        return LocalDateTime.parse(dateTimeString, DateTimeFormatter.ISO_DATE_TIME);
    }



}