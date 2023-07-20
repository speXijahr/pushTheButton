package cz.etn.ptb.repo;

import cz.etn.ptb.dbo.ButtonState;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ButtonStateRepo extends MongoRepository<ButtonState, String> {
}
