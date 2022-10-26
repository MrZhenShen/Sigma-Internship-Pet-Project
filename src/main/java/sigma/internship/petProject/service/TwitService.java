package sigma.internship.petProject.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sigma.internship.petProject.dto.TwitDto;

public interface TwitService {
    Page<TwitDto> findAll(Pageable pageable);

    TwitDto findById(long id);
}
