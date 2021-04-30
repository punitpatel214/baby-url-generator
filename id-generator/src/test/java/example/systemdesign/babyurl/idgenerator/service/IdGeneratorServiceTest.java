package example.systemdesign.babyurl.idgenerator.service;

import example.systemdesign.babyurl.idgenerator.config.ApplicationConfig;
import example.systemdesign.babyurl.idgenerator.dao.IdDao;
import example.systemdesign.babyurl.idgenerator.domain.IdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IdGeneratorServiceTest {

    @Mock
    private IdDao idDao;

    @Mock
    private IdGenerator idGenerator;

    @Mock
    private ApplicationConfig applicationConfig;

    @InjectMocks
    private IdGeneratorService idGeneratorService;


    @BeforeEach
    void setUp() {
        idGeneratorService = new IdGeneratorService(idDao, idGenerator, applicationConfig);
    }

    @Test
    void shouldGetId() {
        when(applicationConfig.getCacheSize()).thenReturn(1);
        when(idDao.getIds(applicationConfig.getCacheSize())).thenReturn(singletonList("id"));
        idGeneratorService.init();

        String id = idGeneratorService.getId();
        assertEquals("id" ,id);
    }

    @Test
    void shouldGenerate() {
    }
}