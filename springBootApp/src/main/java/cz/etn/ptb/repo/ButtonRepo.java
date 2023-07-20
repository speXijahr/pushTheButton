package cz.etn.ptb.repo;

import cz.etn.ptb.dbo.ButtonDBO;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ButtonRepo extends MongoRepository<ButtonDBO, String> {
}
