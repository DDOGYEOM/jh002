package net.openobject.web.test;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;
import net.openobject.domain.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/test")
public class TestController {

    private final Logger log = LoggerFactory.getLogger(TestController.class);

    @PersistenceContext
    private EntityManager entityManager;

    @RequestMapping("/getPrj")
    public ResponseEntity<List<Project>> getPrj(@Valid String name) {
        log.debug("test getPrj >> {}", name);
        List<Project> result = entityManager
            .createQuery(" select e from Project e" + " where name like ?1 ", Project.class)
            .setParameter(1, "%" + name + "%")
            .getResultList();
        log.debug("test getPrj <<\n {}", result);
        return ResponseEntity.ok().body(result);
    }
}
