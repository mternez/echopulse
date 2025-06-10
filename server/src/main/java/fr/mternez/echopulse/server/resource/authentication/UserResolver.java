package fr.mternez.echopulse.server.resource.authentication;

import fr.mternez.echopulse.core.common.domain.model.User;
import fr.mternez.echopulse.core.server.application.command.CreateUserCmd;
import fr.mternez.echopulse.core.common.domain.error.PersistenceException;
import fr.mternez.echopulse.core.server.port.in.UserCommandService;
import fr.mternez.echopulse.server.data.mapper.DomainModelMapper;
import fr.mternez.echopulse.server.data.model.UserEntity;
import fr.mternez.echopulse.server.data.repository.UserEntityRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class UserResolver {

    private final UserCommandService userCommandService;
    private final UserEntityRepository userEntityRepository;
    private final DomainModelMapper domainModelMapper;

    UserResolver(
            final UserCommandService userCommandService,
            final UserEntityRepository userEntityRepository,
            final DomainModelMapper domainModelMapper
    ){
        this.userCommandService = userCommandService;
        this.userEntityRepository = userEntityRepository;
        this.domainModelMapper = domainModelMapper;
    }

    @Transactional
    public User resolve(final Jwt token) throws PersistenceException {

        final String username = token.getClaim("preferred_username");

        final UserEntity user = this.userEntityRepository.findUserEntityByUsername(username);

        if(user == null) {
            return this.userCommandService.execute(new CreateUserCmd(null, username));
        }

        return this.domainModelMapper.toDomainModel(user);
    }
}
