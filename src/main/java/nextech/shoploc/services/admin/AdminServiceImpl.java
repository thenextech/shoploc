package nextech.shoploc.services.admin;

import nextech.shoploc.domains.Admin;
import nextech.shoploc.models.admin.AdminRequestDTO;
import nextech.shoploc.models.admin.AdminResponseDTO;
import nextech.shoploc.repositories.AdminRepository;
import nextech.shoploc.repositories.OrderRepository;
import nextech.shoploc.repositories.UserRepository;
import nextech.shoploc.utils.ModelMapperUtils;
import nextech.shoploc.utils.exceptions.NotFoundException;
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
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {
    private final AdminRepository adminRepository;
    private final ModelMapperUtils modelMapperUtils;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public AdminServiceImpl(AdminRepository adminRepository, ModelMapperUtils modelMapperUtils, OrderRepository orderRepository, UserRepository userRepository) {
        this.adminRepository = adminRepository;
        this.modelMapperUtils = modelMapperUtils;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    @Override
    public AdminResponseDTO createAdmin(AdminRequestDTO adminRequestDTO) {
        Admin admin = modelMapperUtils.getModelMapper().map(adminRequestDTO, Admin.class);
        admin = adminRepository.save(admin);
        return modelMapperUtils.getModelMapper().map(admin, AdminResponseDTO.class);
    }

    @Override
    public AdminResponseDTO getAdminById(Long id) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Admin not found with ID: " + id));
        return modelMapperUtils.getModelMapper().map(admin, AdminResponseDTO.class);
    }

    @Override
    public AdminResponseDTO getAdminByEmail(String email) {
        Admin admin = adminRepository.findAdminByEmail(email)
                .orElseThrow(() -> new NotFoundException("Admin not found with email: " + email));
        return modelMapperUtils.getModelMapper().map(admin, AdminResponseDTO.class);
    }

    @Override
    public List<AdminResponseDTO> getAllAdmins() {
        List<Admin> admins = adminRepository.findAll();
        return admins.stream()
                .map(admin -> modelMapperUtils.getModelMapper().map(admin, AdminResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAdmin(Long id) {
        if (!adminRepository.existsById(id)) {
            throw new NotFoundException("Admin not found with ID: " + id);
        }
        adminRepository.deleteById(id);
    }

    @Override
    public AdminResponseDTO updateAdmin(Long id, AdminRequestDTO adminRequestDTO) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Admin not found with ID: " + id));

        modelMapperUtils.getModelMapper().map(adminRequestDTO, admin);
        admin = adminRepository.save(admin);
        return modelMapperUtils.getModelMapper().map(admin, AdminResponseDTO.class);
    }

    @Override
    public Map<String, Object> getSalesStatistics(String startDate, String endDate) {
        LocalDateTime startDateTime = parseDateTime(startDate, LocalDateTime.now().with(LocalTime.MIN));
        LocalDateTime endDateTime = parseDateTime(endDate, LocalDateTime.now().with(LocalTime.MAX));

        Map<String, Object> statisticsMap = new HashMap<>();
        statisticsMap.put("vfpClientsCount", adminRepository.countByVfp());
        statisticsMap.put("nbClients", adminRepository.countClients());
        statisticsMap.put("nombreTotalClients", orderRepository.nombreTotalClients(startDateTime, endDateTime));
        statisticsMap.put("listeTypesMerchants", adminRepository.listeTypesMerchants());
        statisticsMap.put("countBenefitsClients", adminRepository.countBenefitsClients());
        statisticsMap.put("countClientsOrdered", adminRepository.countClientsOrdered());
        statisticsMap.put("calculateAllTotalRevenue", adminRepository.calculateAllTotalRevenue());
        statisticsMap.put("calculateTotalCostOfUsedBenefits", adminRepository.calculateTotalCostOfUsedBenefits());
        statisticsMap.put("calculateRevenuePerMerchant", adminRepository.calculateRevenuePerMerchant());
        // Nombre total de commandes et de clients distincts sur la période spécifiée
        statisticsMap.put("nbrOrdersBetweenTwoDates", orderRepository.findOrdersBetweenDatesForAdmin(startDateTime, endDateTime).size());

        // Début de la semaine courante et passée
        LocalDateTime startOfThisWeek = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).atStartOfDay();
        LocalDateTime startOfLastWeek = startOfThisWeek.minusWeeks(1);

        // Calculer et stocker le revenu de la semaine courante
        statisticsMap.put("weekRevenue", orderRepository.calculateTodayRevenueForAdmin(startOfThisWeek, LocalDateTime.now()));

        // Calculer et stocker le revenu de la semaine passée
        statisticsMap.put("lastWeekRevenue", orderRepository.calculateTodayRevenueForAdmin(startOfLastWeek, startOfThisWeek.minusSeconds(1)));

        // Calcul du revenu de l'année en cours
        LocalDate startOfCurrentYear = LocalDate.now().withDayOfYear(1);
        LocalDate endOfCurrentYear = startOfCurrentYear.with(TemporalAdjusters.lastDayOfYear());
        Double currentYearRevenue = orderRepository.calculateTodayRevenueForAdmin( startOfCurrentYear.atStartOfDay(), endOfCurrentYear.atTime(LocalTime.MAX));
        statisticsMap.put("yearRevenue", currentYearRevenue);

        // Calcul du revenu de l'année précédente
        LocalDate startOfLastYear = LocalDate.now().minusYears(1).withDayOfYear(1);
        LocalDate endOfLastYear = startOfLastYear.with(TemporalAdjusters.lastDayOfYear());
        Double lastYearRevenue = orderRepository.calculateTodayRevenueForAdmin(startOfLastYear.atStartOfDay(), endOfLastYear.atTime(LocalTime.MAX));
        statisticsMap.put("lastYearRevenue", lastYearRevenue);

        // Calcul du revenu du mois en cours
        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate endOfMonth = startOfMonth.with(TemporalAdjusters.lastDayOfMonth()); // Ajusté pour la fin du mois
        Double monthRevenue = orderRepository.calculateTodayRevenueForAdmin( startOfMonth.atStartOfDay(), endOfMonth.atTime(LocalTime.MAX));
        statisticsMap.put("monthRevenue", monthRevenue);

        // Calcul du revenu du mois précédent
        LocalDate startOfLastMonth = LocalDate.now().minusMonths(1).withDayOfMonth(1);
        LocalDate endOfLastMonth = startOfLastMonth.with(TemporalAdjusters.lastDayOfMonth()); // Obtient le dernier jour du mois précédent
        Double lastMonthRevenue = orderRepository.calculateTodayRevenueForAdmin(startOfLastMonth.atStartOfDay(), endOfLastMonth.atTime(LocalTime.MAX));
        statisticsMap.put("lastMonthRevenue", lastMonthRevenue);
        return statisticsMap;
    }

    private LocalDateTime parseDateTime(String dateTimeString, LocalDateTime defaultValue) {
        if (dateTimeString == null || dateTimeString.isEmpty()) {
            return defaultValue;
        }
        return LocalDateTime.parse(dateTimeString, DateTimeFormatter.ISO_DATE_TIME);
    }


}

