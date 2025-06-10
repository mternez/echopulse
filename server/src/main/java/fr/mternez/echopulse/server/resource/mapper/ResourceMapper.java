package fr.mternez.echopulse.server.resource.mapper;

import fr.mternez.echopulse.core.common.domain.model.*;
import fr.mternez.echopulse.server.data.projection.*;
import fr.mternez.echopulse.server.resource.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface ResourceMapper {

    RoleDetailsResource toResource(RoleDetails details);
    ServerDetailsResource toResource(ServerDetails details);
    ChannelDetailsResource toResource(ChannelDetails details);
    ServerMemberDetailsResource toResource(ServerMemberDetails details);
    @Mapping(source = "model.id", target = "id", qualifiedByName = "serverIdToUUID")
    ServerDetailsResource toResource(Server model);
    @Mapping(source = "model.id", target = "id", qualifiedByName = "channelIdToUUID")
    ChannelDetailsResource toResource(Channel model);
    MembershipSummaryResource toResource(MembershipSummary summary);
    ServerSummaryResource toResource(ServerSummary summary);
    UserSummaryResource toResource(UserSummary summary);

    @Named("serverIdToUUID")
    default UUID serverIdToUUID(final ServerId serverId) {
        return serverId.getValue();
    }

    @Named("channelIdToUUID")
    default UUID channelIdToUUID(final ChannelId channelId) {
        return channelId.getValue();
    }
}
