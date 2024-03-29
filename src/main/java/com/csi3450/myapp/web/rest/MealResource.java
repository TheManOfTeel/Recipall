package com.csi3450.myapp.web.rest;

import com.csi3450.myapp.domain.Meal;
import com.csi3450.myapp.repository.MealRepository;
import com.csi3450.myapp.repository.search.MealSearchRepository;
import com.csi3450.myapp.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link com.csi3450.myapp.domain.Meal}.
 */
@RestController
@RequestMapping("/api")
public class MealResource {

    private final Logger log = LoggerFactory.getLogger(MealResource.class);

    private static final String ENTITY_NAME = "meal";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MealRepository mealRepository;

    private final MealSearchRepository mealSearchRepository;

    public MealResource(MealRepository mealRepository, MealSearchRepository mealSearchRepository) {
        this.mealRepository = mealRepository;
        this.mealSearchRepository = mealSearchRepository;
    }

    /**
     * {@code POST  /meals} : Create a new meal.
     *
     * @param meal the meal to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new meal, or with status {@code 400 (Bad Request)} if the meal has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/meals")
    public ResponseEntity<Meal> createMeal(@Valid @RequestBody Meal meal) throws URISyntaxException {
        log.debug("REST request to save Meal : {}", meal);
        if (meal.getId() != null) {
            throw new BadRequestAlertException("A new meal cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Meal result = mealRepository.save(meal);
        mealSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/meals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /meals} : Updates an existing meal.
     *
     * @param meal the meal to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated meal,
     * or with status {@code 400 (Bad Request)} if the meal is not valid,
     * or with status {@code 500 (Internal Server Error)} if the meal couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/meals")
    public ResponseEntity<Meal> updateMeal(@Valid @RequestBody Meal meal) throws URISyntaxException {
        log.debug("REST request to update Meal : {}", meal);
        if (meal.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Meal result = mealRepository.save(meal);
        mealSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, meal.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /meals} : get all the meals.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of meals in body.
     */
    @GetMapping("/meals")
    public List<Meal> getAllMeals() {
        log.debug("REST request to get all Meals");
        return mealRepository.findAll();
    }

    /**
     * {@code GET  /meals/:id} : get the "id" meal.
     *
     * @param id the id of the meal to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the meal, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/meals/{id}")
    public ResponseEntity<Meal> getMeal(@PathVariable Long id) {
        log.debug("REST request to get Meal : {}", id);
        Optional<Meal> meal = mealRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(meal);
    }

    /**
     * {@code DELETE  /meals/:id} : delete the "id" meal.
     *
     * @param id the id of the meal to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/meals/{id}")
    public ResponseEntity<Void> deleteMeal(@PathVariable Long id) {
        log.debug("REST request to delete Meal : {}", id);
        mealRepository.deleteById(id);
        mealSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/meals?query=:query} : search for the meal corresponding
     * to the query.
     *
     * @param query the query of the meal search.
     * @return the result of the search.
     */
    @GetMapping("/_search/meals")
    public List<Meal> searchMeals(@RequestParam String query) {
        log.debug("REST request to search Meals for query {}", query);
        return StreamSupport
            .stream(mealSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
