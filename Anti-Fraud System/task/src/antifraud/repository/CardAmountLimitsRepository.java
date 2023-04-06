package antifraud.repository;

import antifraud.model.CardAmountLimits;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardAmountLimitsRepository extends JpaRepository<CardAmountLimits, String> {
}
