package antifraud.repository;

import antifraud.model.IpAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IpAddressRepository extends JpaRepository<IpAddress, Long> {
    Optional<IpAddress> findByIp(String ip);
}
