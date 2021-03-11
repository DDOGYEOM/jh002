package net.openobject.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import javax.persistence.EntityManager;
import net.openobject.IntegrationTest;
import net.openobject.domain.Label;
import net.openobject.domain.Ticket;
import net.openobject.repository.LabelRepository;
import net.openobject.service.LabelQueryService;
import net.openobject.service.dto.LabelCriteria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link LabelResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class LabelResourceIT {

    private static final String DEFAULT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_LABEL = "BBBBBBBBBB";

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private LabelQueryService labelQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLabelMockMvc;

    private Label label;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Label createEntity(EntityManager em) {
        Label label = new Label().label(DEFAULT_LABEL);
        return label;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Label createUpdatedEntity(EntityManager em) {
        Label label = new Label().label(UPDATED_LABEL);
        return label;
    }

    @BeforeEach
    public void initTest() {
        label = createEntity(em);
    }

    @Test
    @Transactional
    void createLabel() throws Exception {
        int databaseSizeBeforeCreate = labelRepository.findAll().size();
        // Create the Label
        restLabelMockMvc
            .perform(
                post("/api/labels").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(label))
            )
            .andExpect(status().isCreated());

        // Validate the Label in the database
        List<Label> labelList = labelRepository.findAll();
        assertThat(labelList).hasSize(databaseSizeBeforeCreate + 1);
        Label testLabel = labelList.get(labelList.size() - 1);
        assertThat(testLabel.getLabel()).isEqualTo(DEFAULT_LABEL);
    }

    @Test
    @Transactional
    void createLabelWithExistingId() throws Exception {
        // Create the Label with an existing ID
        label.setId(1L);

        int databaseSizeBeforeCreate = labelRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLabelMockMvc
            .perform(
                post("/api/labels").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(label))
            )
            .andExpect(status().isBadRequest());

        // Validate the Label in the database
        List<Label> labelList = labelRepository.findAll();
        assertThat(labelList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLabelIsRequired() throws Exception {
        int databaseSizeBeforeTest = labelRepository.findAll().size();
        // set the field null
        label.setLabel(null);

        // Create the Label, which fails.

        restLabelMockMvc
            .perform(
                post("/api/labels").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(label))
            )
            .andExpect(status().isBadRequest());

        List<Label> labelList = labelRepository.findAll();
        assertThat(labelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLabels() throws Exception {
        // Initialize the database
        labelRepository.saveAndFlush(label);

        // Get all the labelList
        restLabelMockMvc
            .perform(get("/api/labels?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(label.getId().intValue())))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)));
    }

    @Test
    @Transactional
    void getLabel() throws Exception {
        // Initialize the database
        labelRepository.saveAndFlush(label);

        // Get the label
        restLabelMockMvc
            .perform(get("/api/labels/{id}", label.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(label.getId().intValue()))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL));
    }

    @Test
    @Transactional
    void getLabelsByIdFiltering() throws Exception {
        // Initialize the database
        labelRepository.saveAndFlush(label);

        Long id = label.getId();

        defaultLabelShouldBeFound("id.equals=" + id);
        defaultLabelShouldNotBeFound("id.notEquals=" + id);

        defaultLabelShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLabelShouldNotBeFound("id.greaterThan=" + id);

        defaultLabelShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLabelShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLabelsByLabelIsEqualToSomething() throws Exception {
        // Initialize the database
        labelRepository.saveAndFlush(label);

        // Get all the labelList where label equals to DEFAULT_LABEL
        defaultLabelShouldBeFound("label.equals=" + DEFAULT_LABEL);

        // Get all the labelList where label equals to UPDATED_LABEL
        defaultLabelShouldNotBeFound("label.equals=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    void getAllLabelsByLabelIsNotEqualToSomething() throws Exception {
        // Initialize the database
        labelRepository.saveAndFlush(label);

        // Get all the labelList where label not equals to DEFAULT_LABEL
        defaultLabelShouldNotBeFound("label.notEquals=" + DEFAULT_LABEL);

        // Get all the labelList where label not equals to UPDATED_LABEL
        defaultLabelShouldBeFound("label.notEquals=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    void getAllLabelsByLabelIsInShouldWork() throws Exception {
        // Initialize the database
        labelRepository.saveAndFlush(label);

        // Get all the labelList where label in DEFAULT_LABEL or UPDATED_LABEL
        defaultLabelShouldBeFound("label.in=" + DEFAULT_LABEL + "," + UPDATED_LABEL);

        // Get all the labelList where label equals to UPDATED_LABEL
        defaultLabelShouldNotBeFound("label.in=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    void getAllLabelsByLabelIsNullOrNotNull() throws Exception {
        // Initialize the database
        labelRepository.saveAndFlush(label);

        // Get all the labelList where label is not null
        defaultLabelShouldBeFound("label.specified=true");

        // Get all the labelList where label is null
        defaultLabelShouldNotBeFound("label.specified=false");
    }

    @Test
    @Transactional
    void getAllLabelsByLabelContainsSomething() throws Exception {
        // Initialize the database
        labelRepository.saveAndFlush(label);

        // Get all the labelList where label contains DEFAULT_LABEL
        defaultLabelShouldBeFound("label.contains=" + DEFAULT_LABEL);

        // Get all the labelList where label contains UPDATED_LABEL
        defaultLabelShouldNotBeFound("label.contains=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    void getAllLabelsByLabelNotContainsSomething() throws Exception {
        // Initialize the database
        labelRepository.saveAndFlush(label);

        // Get all the labelList where label does not contain DEFAULT_LABEL
        defaultLabelShouldNotBeFound("label.doesNotContain=" + DEFAULT_LABEL);

        // Get all the labelList where label does not contain UPDATED_LABEL
        defaultLabelShouldBeFound("label.doesNotContain=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    void getAllLabelsByTicketIsEqualToSomething() throws Exception {
        // Initialize the database
        labelRepository.saveAndFlush(label);
        Ticket ticket = TicketResourceIT.createEntity(em);
        em.persist(ticket);
        em.flush();
        label.addTicket(ticket);
        labelRepository.saveAndFlush(label);
        Long ticketId = ticket.getId();

        // Get all the labelList where ticket equals to ticketId
        defaultLabelShouldBeFound("ticketId.equals=" + ticketId);

        // Get all the labelList where ticket equals to ticketId + 1
        defaultLabelShouldNotBeFound("ticketId.equals=" + (ticketId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLabelShouldBeFound(String filter) throws Exception {
        restLabelMockMvc
            .perform(get("/api/labels?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(label.getId().intValue())))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)));

        // Check, that the count call also returns 1
        restLabelMockMvc
            .perform(get("/api/labels/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLabelShouldNotBeFound(String filter) throws Exception {
        restLabelMockMvc
            .perform(get("/api/labels?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLabelMockMvc
            .perform(get("/api/labels/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLabel() throws Exception {
        // Get the label
        restLabelMockMvc.perform(get("/api/labels/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void updateLabel() throws Exception {
        // Initialize the database
        labelRepository.saveAndFlush(label);

        int databaseSizeBeforeUpdate = labelRepository.findAll().size();

        // Update the label
        Label updatedLabel = labelRepository.findById(label.getId()).get();
        // Disconnect from session so that the updates on updatedLabel are not directly saved in db
        em.detach(updatedLabel);
        updatedLabel.label(UPDATED_LABEL);

        restLabelMockMvc
            .perform(
                put("/api/labels")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedLabel))
            )
            .andExpect(status().isOk());

        // Validate the Label in the database
        List<Label> labelList = labelRepository.findAll();
        assertThat(labelList).hasSize(databaseSizeBeforeUpdate);
        Label testLabel = labelList.get(labelList.size() - 1);
        assertThat(testLabel.getLabel()).isEqualTo(UPDATED_LABEL);
    }

    @Test
    @Transactional
    void updateNonExistingLabel() throws Exception {
        int databaseSizeBeforeUpdate = labelRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLabelMockMvc
            .perform(
                put("/api/labels").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(label))
            )
            .andExpect(status().isBadRequest());

        // Validate the Label in the database
        List<Label> labelList = labelRepository.findAll();
        assertThat(labelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLabelWithPatch() throws Exception {
        // Initialize the database
        labelRepository.saveAndFlush(label);

        int databaseSizeBeforeUpdate = labelRepository.findAll().size();

        // Update the label using partial update
        Label partialUpdatedLabel = new Label();
        partialUpdatedLabel.setId(label.getId());

        restLabelMockMvc
            .perform(
                patch("/api/labels")
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLabel))
            )
            .andExpect(status().isOk());

        // Validate the Label in the database
        List<Label> labelList = labelRepository.findAll();
        assertThat(labelList).hasSize(databaseSizeBeforeUpdate);
        Label testLabel = labelList.get(labelList.size() - 1);
        assertThat(testLabel.getLabel()).isEqualTo(DEFAULT_LABEL);
    }

    @Test
    @Transactional
    void fullUpdateLabelWithPatch() throws Exception {
        // Initialize the database
        labelRepository.saveAndFlush(label);

        int databaseSizeBeforeUpdate = labelRepository.findAll().size();

        // Update the label using partial update
        Label partialUpdatedLabel = new Label();
        partialUpdatedLabel.setId(label.getId());

        partialUpdatedLabel.label(UPDATED_LABEL);

        restLabelMockMvc
            .perform(
                patch("/api/labels")
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLabel))
            )
            .andExpect(status().isOk());

        // Validate the Label in the database
        List<Label> labelList = labelRepository.findAll();
        assertThat(labelList).hasSize(databaseSizeBeforeUpdate);
        Label testLabel = labelList.get(labelList.size() - 1);
        assertThat(testLabel.getLabel()).isEqualTo(UPDATED_LABEL);
    }

    @Test
    @Transactional
    void partialUpdateLabelShouldThrown() throws Exception {
        // Update the label without id should throw
        Label partialUpdatedLabel = new Label();

        restLabelMockMvc
            .perform(
                patch("/api/labels")
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLabel))
            )
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void deleteLabel() throws Exception {
        // Initialize the database
        labelRepository.saveAndFlush(label);

        int databaseSizeBeforeDelete = labelRepository.findAll().size();

        // Delete the label
        restLabelMockMvc
            .perform(delete("/api/labels/{id}", label.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Label> labelList = labelRepository.findAll();
        assertThat(labelList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
