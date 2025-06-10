package fr.mternez.echopulse.server.resource.controller;


import fr.mternez.echopulse.core.common.domain.model.User;
import fr.mternez.echopulse.server.data.projection.UserSummary;
import fr.mternez.echopulse.server.data.repository.UserEntityRepository;
import fr.mternez.echopulse.server.resource.authentication.AuthenticatedUser;
import fr.mternez.echopulse.server.resource.mapper.ResourceMapper;
import fr.mternez.echopulse.server.resource.model.UserSummaryResource;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Transactional
@RequestMapping(path = "/api/users")
public class UserController {

    private final UserEntityRepository userEntityRepository;
    private final ResourceMapper resourceMapper;

    UserController(
            final UserEntityRepository userEntityRepository,
            final ResourceMapper resourceMapper
    ) {
        this.userEntityRepository = userEntityRepository;
        this.resourceMapper = resourceMapper;
    }

    @GetMapping
    public ResponseEntity<UserSummaryResource> getSelf(@AuthenticatedUser User self) {

        return this.userEntityRepository.findByUsername(self.getUsername(), UserSummary.class)
                .map(this.resourceMapper::toResource)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

