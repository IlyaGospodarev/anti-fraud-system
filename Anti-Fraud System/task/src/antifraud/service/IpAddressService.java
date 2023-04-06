package antifraud.service;

import antifraud.dto.IpRequest;
import antifraud.dto.IpResponse;
import antifraud.dto.StatusResponse;
import antifraud.exception.BadRequestException;
import antifraud.exception.HttpConflictException;
import antifraud.exception.NotFoundException;
import antifraud.model.IpAddress;
import antifraud.repository.IpAddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class IpAddressService {
    public static final String REGEXP = "^((\\d|[1-9]\\d|1\\d{2}|2[0-5]{2})\\.){3}(\\d|[1-9]\\d|1\\d{2}|2[0-5]{2})$";
    @Autowired
    IpAddressRepository repository;

    public IpResponse addIpAddress(IpRequest ipRequest) {
        Optional<IpAddress> optionalIpAddress = repository.findByIp(ipRequest.getIp());
        if (optionalIpAddress.isPresent()) {
            throw new HttpConflictException("Duplicate IP");
        }

        boolean matches = ipRequest.getIp().matches(REGEXP);
        if (!matches) {
            throw new BadRequestException("Invalid IP format");
        }

        IpAddress ipAddress = new IpAddress();
        ipAddress.setIp(ipRequest.getIp());
        IpAddress savedIpAddress = repository.save(ipAddress);

        return new IpResponse(savedIpAddress);
    }

    public StatusResponse deleteSuspiciousIP(String ip) {

        boolean matches = ip.matches(REGEXP);
        if (!matches) {
            throw new BadRequestException("Invalid IP format");
        }

        Optional<IpAddress> optionalIpAddress = repository.findByIp(ip);

        if (optionalIpAddress.isPresent()) {
            IpAddress ipAddress = optionalIpAddress.get();
            repository.delete(ipAddress);
            return new StatusResponse(String.format("IP %s successfully removed!", ipAddress.getIp()));
        } else {
            throw new NotFoundException("IP is not found in the database");
        }
    }

    public List<IpResponse> getAllSuspiciousIPs() {
        return repository.findAll().stream().map(IpResponse::new).collect(Collectors.toList());
    }
}
