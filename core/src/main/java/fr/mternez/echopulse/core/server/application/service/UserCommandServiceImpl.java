package fr.mternez.echopulse.core.server.application.service;

import fr.mternez.echopulse.core.common.domain.model.User;
import fr.mternez.echopulse.core.common.domain.model.UserId;
import fr.mternez.echopulse.core.server.application.command.CreateUserCmd;
import fr.mternez.echopulse.core.common.domain.error.PersistenceException;
import fr.mternez.echopulse.core.server.port.in.UserCommandService;
import fr.mternez.echopulse.core.server.port.out.UserRepository;

import java.util.logging.Logger;

public class UserCommandServiceImpl implements UserCommandService {

    private static final Logger logger = Logger.getLogger(UserCommandServiceImpl.class.getName());

    private final UserRepository userRepository;

    public UserCommandServiceImpl(
            final UserRepository userRepository
    ) {
        this.userRepository = userRepository;
    }

    @Override
    public User execute(final CreateUserCmd cmd) throws PersistenceException {

        return this.userRepository.persistNew(new User(new UserId(), cmd.username()));
    }
}
