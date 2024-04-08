package nextech.shoploc.schedules;

import nextech.shoploc.domains.Client;
import nextech.shoploc.repositories.ClientRepository;
import nextech.shoploc.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@EnableScheduling
public class ClientsStatusBatchScheduler {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ApplicationContext applicationContext;

    @Scheduled(fixedDelayString = "${duration.update.status.clients.batch.seconds:60}")
    public void updateAllClientsStatusBatch() {
        if (!(applicationContext instanceof ConfigurableApplicationContext) ||
                !((ConfigurableApplicationContext) applicationContext).isActive()) {
            return;
        }

        List<Client> clients = clientRepository.findAll();
        clients.forEach(client -> {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime fifteenDaysAgo = now.minusDays(15);
            Long ordersCount = orderRepository.countByUserIdAndCreationDateBetween(client.getUserId(), fifteenDaysAgo, now);
            if (client.getLastDateTimeVFPActivated() == null || now.isAfter(client.getLastDateTimeVFPActivated().plusDays(15))) {
                // Si le client n'a jamais eu le statut VFP ou si les 15 jours depuis la dernière activation sont écoulés
                if (ordersCount >= 10) {
                    client.setVfp(true);
                    client.setLastDateTimeVFPActivated(now);
                } else {
                    client.setVfp(false);
                }
            } else if (ordersCount < 10 && now.isAfter(client.getLastDateTimeVFPActivated().plusDays(15))) {
                // Si moins de 10 achats ont été faits dans les 15 jours après la dernière activation
                client.setVfp(false);
            }
            // Pas besoin de modifier le statut ou la date si le client conserve son statut VFP avec plus de 10 achats dans les 15 derniers jours

            clientRepository.save(client);
        });
    }
}
