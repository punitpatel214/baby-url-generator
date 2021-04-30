package example.systemdesign.babyurl.idgenerator.api;


import example.systemdesign.babyurl.idgenerator.service.IdGeneratorService;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

@Controller(IdGeneratorController.ID_GENERATION_API_ENDPOINT)
public class IdGeneratorController {

    public static final String ID_GENERATION_API_ENDPOINT = "/generate";

    private IdGeneratorService idGeneratorService;

    public IdGeneratorController(IdGeneratorService idGeneratorService) {
        this.idGeneratorService = idGeneratorService;
    }

    @Get(produces = MediaType.TEXT_PLAIN)
    public String generateId() {
        return idGeneratorService.getId();
    }

}
