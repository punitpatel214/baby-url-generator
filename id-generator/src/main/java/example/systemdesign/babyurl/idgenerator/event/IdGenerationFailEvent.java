package example.systemdesign.babyurl.idgenerator.event;

import javax.inject.Singleton;

@Singleton
public class IdGenerationFailEvent {
    private long numberOfIdsAvailable;
}
