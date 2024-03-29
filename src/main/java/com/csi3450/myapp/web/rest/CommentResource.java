package com.csi3450.myapp.web.rest;

import com.csi3450.myapp.domain.Comment;
import com.csi3450.myapp.repository.CommentRepository;
import com.csi3450.myapp.repository.search.CommentSearchRepository;
import com.csi3450.myapp.security.AuthoritiesConstants;
import com.csi3450.myapp.security.SecurityUtils;
import com.csi3450.myapp.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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
 * REST controller for managing {@link com.csi3450.myapp.domain.Comment}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CommentResource {

    private final Logger log = LoggerFactory.getLogger(CommentResource.class);

    private static final String ENTITY_NAME = "comment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CommentRepository commentRepository;

    private final CommentSearchRepository commentSearchRepository;

    public CommentResource(CommentRepository commentRepository, CommentSearchRepository commentSearchRepository) {
        this.commentRepository = commentRepository;
        this.commentSearchRepository = commentSearchRepository;
    }

    /**
     * {@code POST  /comments} : Create a new comment.
     *
     * @param comment the comment to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new comment, or with status {@code 400 (Bad Request)} if the comment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/comments")
    public ResponseEntity<Comment> createComment(@Valid @RequestBody Comment comment) throws URISyntaxException {
        log.debug("REST request to save Comment : {}", comment);
        if (comment.getId() != null) {
            throw new BadRequestAlertException("A new comment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Comment result = commentRepository.save(comment);
        commentSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/comments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /comments} : Updates an existing comment.
     *
     * @param comment the comment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated comment,
     * or with status {@code 400 (Bad Request)} if the comment is not valid,
     * or with status {@code 500 (Internal Server Error)} if the comment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/comments")
    public ResponseEntity<?> updateComment(@Valid @RequestBody Comment comment) throws URISyntaxException {
        log.debug("REST request to update Comment : {}", comment);
        if (comment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (comment.getUser() != null &&
            !comment.getUser().getLogin().equals(SecurityUtils.getCurrentUserLogin().orElse(""))) {
            return new ResponseEntity<>("error.http.403", HttpStatus.FORBIDDEN);
        }
        Comment result = commentRepository.save(comment);
        commentSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, comment.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /comments} : get all the comments.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of comments in body.
     */
    @GetMapping("/comments")
    public List<Comment> getAllComments() {
        log.debug("REST request to get all Comments");
        return commentRepository.findAll();
    }

    @GetMapping("/comments/byrecipe/{id}")
    public List<Comment> getCommentByRecipeId(@PathVariable Long id) {
        log.debug("REST request to get Comments by Recipe ID : {}", id);
        return commentRepository.findByRecipeId(id);
    }

    /**
     * {@code GET  /comments/:id} : get the "id" comment.
     *
     * @param id the id of the comment to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the comment, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/comments/{id}")
    public ResponseEntity<Comment> getComment(@PathVariable Long id) {
        log.debug("REST request to get Comment : {}", id);
        Optional<Comment> comment = commentRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(comment);
    }

    /**
     * {@code DELETE  /comments/:id} : delete the "id" comment.
     *
     * @param id the id of the comment to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/comments/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable Long id) {
        log.debug("REST request to delete Comment : {}", id);
        Optional<Comment> comment = commentRepository.findById(id);
        boolean ownedByCurrentUser = comment.isPresent() && comment.get().getUser() != null &&
            comment.get().getUser().getLogin().equals(SecurityUtils.getCurrentUserLogin().orElse(""));
        if (!ownedByCurrentUser && !SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) {
            return new ResponseEntity<>("error.http.403", HttpStatus.FORBIDDEN);
        }
        commentRepository.deleteById(id);
        commentSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/comments?query=:query} : search for the comment corresponding
     * to the query.
     *
     * @param query the query of the comment search.
     * @return the result of the search.
     */
    @GetMapping("/_search/comments")
    public List<Comment> searchComments(@RequestParam String query) {
        log.debug("REST request to search Comments for query {}", query);
        return StreamSupport
            .stream(commentSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
