package net.openobject.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import javax.persistence.EntityManager;
import net.openobject.IntegrationTest;
import net.openobject.domain.Etest;
import net.openobject.repository.EtestRepository;
import net.openobject.service.EtestQueryService;
import net.openobject.service.dto.EtestCriteria;
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
 * Integration tests for the {@link EtestResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class EtestResourceIT {

    private static final String DEFAULT_TESTNAME = "AAAAAAAAAA";
    private static final String UPDATED_TESTNAME = "BBBBBBBBBB";

    private static final Long DEFAULT_TESTNUM = 1L;
    private static final Long UPDATED_TESTNUM = 2L;
    private static final Long SMALLER_TESTNUM = 1L - 1L;

    private static final String DEFAULT_TESTADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_TESTADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_TESTPHONE = "AAAAAAAAAA";
    private static final String UPDATED_TESTPHONE = "BBBBBBBBBB";

    @Autowired
    private EtestRepository etestRepository;

    @Autowired
    private EtestQueryService etestQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEtestMockMvc;

    private Etest etest;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Etest createEntity(EntityManager em) {
        Etest etest = new Etest()
            .testname(DEFAULT_TESTNAME)
            .testnum(DEFAULT_TESTNUM)
            .testaddress(DEFAULT_TESTADDRESS)
            .testphone(DEFAULT_TESTPHONE);
        return etest;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Etest createUpdatedEntity(EntityManager em) {
        Etest etest = new Etest()
            .testname(UPDATED_TESTNAME)
            .testnum(UPDATED_TESTNUM)
            .testaddress(UPDATED_TESTADDRESS)
            .testphone(UPDATED_TESTPHONE);
        return etest;
    }

    @BeforeEach
    public void initTest() {
        etest = createEntity(em);
    }

    @Test
    @Transactional
    void createEtest() throws Exception {
        int databaseSizeBeforeCreate = etestRepository.findAll().size();
        // Create the Etest
        restEtestMockMvc
            .perform(
                post("/api/etests").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(etest))
            )
            .andExpect(status().isCreated());

        // Validate the Etest in the database
        List<Etest> etestList = etestRepository.findAll();
        assertThat(etestList).hasSize(databaseSizeBeforeCreate + 1);
        Etest testEtest = etestList.get(etestList.size() - 1);
        assertThat(testEtest.getTestname()).isEqualTo(DEFAULT_TESTNAME);
        assertThat(testEtest.getTestnum()).isEqualTo(DEFAULT_TESTNUM);
        assertThat(testEtest.getTestaddress()).isEqualTo(DEFAULT_TESTADDRESS);
        assertThat(testEtest.getTestphone()).isEqualTo(DEFAULT_TESTPHONE);
    }

    @Test
    @Transactional
    void createEtestWithExistingId() throws Exception {
        // Create the Etest with an existing ID
        etest.setId(1L);

        int databaseSizeBeforeCreate = etestRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEtestMockMvc
            .perform(
                post("/api/etests").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(etest))
            )
            .andExpect(status().isBadRequest());

        // Validate the Etest in the database
        List<Etest> etestList = etestRepository.findAll();
        assertThat(etestList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEtests() throws Exception {
        // Initialize the database
        etestRepository.saveAndFlush(etest);

        // Get all the etestList
        restEtestMockMvc
            .perform(get("/api/etests?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(etest.getId().intValue())))
            .andExpect(jsonPath("$.[*].testname").value(hasItem(DEFAULT_TESTNAME)))
            .andExpect(jsonPath("$.[*].testnum").value(hasItem(DEFAULT_TESTNUM.intValue())))
            .andExpect(jsonPath("$.[*].testaddress").value(hasItem(DEFAULT_TESTADDRESS)))
            .andExpect(jsonPath("$.[*].testphone").value(hasItem(DEFAULT_TESTPHONE)));
    }

    @Test
    @Transactional
    void getEtest() throws Exception {
        // Initialize the database
        etestRepository.saveAndFlush(etest);

        // Get the etest
        restEtestMockMvc
            .perform(get("/api/etests/{id}", etest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(etest.getId().intValue()))
            .andExpect(jsonPath("$.testname").value(DEFAULT_TESTNAME))
            .andExpect(jsonPath("$.testnum").value(DEFAULT_TESTNUM.intValue()))
            .andExpect(jsonPath("$.testaddress").value(DEFAULT_TESTADDRESS))
            .andExpect(jsonPath("$.testphone").value(DEFAULT_TESTPHONE));
    }

    @Test
    @Transactional
    void getEtestsByIdFiltering() throws Exception {
        // Initialize the database
        etestRepository.saveAndFlush(etest);

        Long id = etest.getId();

        defaultEtestShouldBeFound("id.equals=" + id);
        defaultEtestShouldNotBeFound("id.notEquals=" + id);

        defaultEtestShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEtestShouldNotBeFound("id.greaterThan=" + id);

        defaultEtestShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEtestShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEtestsByTestnameIsEqualToSomething() throws Exception {
        // Initialize the database
        etestRepository.saveAndFlush(etest);

        // Get all the etestList where testname equals to DEFAULT_TESTNAME
        defaultEtestShouldBeFound("testname.equals=" + DEFAULT_TESTNAME);

        // Get all the etestList where testname equals to UPDATED_TESTNAME
        defaultEtestShouldNotBeFound("testname.equals=" + UPDATED_TESTNAME);
    }

    @Test
    @Transactional
    void getAllEtestsByTestnameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        etestRepository.saveAndFlush(etest);

        // Get all the etestList where testname not equals to DEFAULT_TESTNAME
        defaultEtestShouldNotBeFound("testname.notEquals=" + DEFAULT_TESTNAME);

        // Get all the etestList where testname not equals to UPDATED_TESTNAME
        defaultEtestShouldBeFound("testname.notEquals=" + UPDATED_TESTNAME);
    }

    @Test
    @Transactional
    void getAllEtestsByTestnameIsInShouldWork() throws Exception {
        // Initialize the database
        etestRepository.saveAndFlush(etest);

        // Get all the etestList where testname in DEFAULT_TESTNAME or UPDATED_TESTNAME
        defaultEtestShouldBeFound("testname.in=" + DEFAULT_TESTNAME + "," + UPDATED_TESTNAME);

        // Get all the etestList where testname equals to UPDATED_TESTNAME
        defaultEtestShouldNotBeFound("testname.in=" + UPDATED_TESTNAME);
    }

    @Test
    @Transactional
    void getAllEtestsByTestnameIsNullOrNotNull() throws Exception {
        // Initialize the database
        etestRepository.saveAndFlush(etest);

        // Get all the etestList where testname is not null
        defaultEtestShouldBeFound("testname.specified=true");

        // Get all the etestList where testname is null
        defaultEtestShouldNotBeFound("testname.specified=false");
    }

    @Test
    @Transactional
    void getAllEtestsByTestnameContainsSomething() throws Exception {
        // Initialize the database
        etestRepository.saveAndFlush(etest);

        // Get all the etestList where testname contains DEFAULT_TESTNAME
        defaultEtestShouldBeFound("testname.contains=" + DEFAULT_TESTNAME);

        // Get all the etestList where testname contains UPDATED_TESTNAME
        defaultEtestShouldNotBeFound("testname.contains=" + UPDATED_TESTNAME);
    }

    @Test
    @Transactional
    void getAllEtestsByTestnameNotContainsSomething() throws Exception {
        // Initialize the database
        etestRepository.saveAndFlush(etest);

        // Get all the etestList where testname does not contain DEFAULT_TESTNAME
        defaultEtestShouldNotBeFound("testname.doesNotContain=" + DEFAULT_TESTNAME);

        // Get all the etestList where testname does not contain UPDATED_TESTNAME
        defaultEtestShouldBeFound("testname.doesNotContain=" + UPDATED_TESTNAME);
    }

    @Test
    @Transactional
    void getAllEtestsByTestnumIsEqualToSomething() throws Exception {
        // Initialize the database
        etestRepository.saveAndFlush(etest);

        // Get all the etestList where testnum equals to DEFAULT_TESTNUM
        defaultEtestShouldBeFound("testnum.equals=" + DEFAULT_TESTNUM);

        // Get all the etestList where testnum equals to UPDATED_TESTNUM
        defaultEtestShouldNotBeFound("testnum.equals=" + UPDATED_TESTNUM);
    }

    @Test
    @Transactional
    void getAllEtestsByTestnumIsNotEqualToSomething() throws Exception {
        // Initialize the database
        etestRepository.saveAndFlush(etest);

        // Get all the etestList where testnum not equals to DEFAULT_TESTNUM
        defaultEtestShouldNotBeFound("testnum.notEquals=" + DEFAULT_TESTNUM);

        // Get all the etestList where testnum not equals to UPDATED_TESTNUM
        defaultEtestShouldBeFound("testnum.notEquals=" + UPDATED_TESTNUM);
    }

    @Test
    @Transactional
    void getAllEtestsByTestnumIsInShouldWork() throws Exception {
        // Initialize the database
        etestRepository.saveAndFlush(etest);

        // Get all the etestList where testnum in DEFAULT_TESTNUM or UPDATED_TESTNUM
        defaultEtestShouldBeFound("testnum.in=" + DEFAULT_TESTNUM + "," + UPDATED_TESTNUM);

        // Get all the etestList where testnum equals to UPDATED_TESTNUM
        defaultEtestShouldNotBeFound("testnum.in=" + UPDATED_TESTNUM);
    }

    @Test
    @Transactional
    void getAllEtestsByTestnumIsNullOrNotNull() throws Exception {
        // Initialize the database
        etestRepository.saveAndFlush(etest);

        // Get all the etestList where testnum is not null
        defaultEtestShouldBeFound("testnum.specified=true");

        // Get all the etestList where testnum is null
        defaultEtestShouldNotBeFound("testnum.specified=false");
    }

    @Test
    @Transactional
    void getAllEtestsByTestnumIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        etestRepository.saveAndFlush(etest);

        // Get all the etestList where testnum is greater than or equal to DEFAULT_TESTNUM
        defaultEtestShouldBeFound("testnum.greaterThanOrEqual=" + DEFAULT_TESTNUM);

        // Get all the etestList where testnum is greater than or equal to UPDATED_TESTNUM
        defaultEtestShouldNotBeFound("testnum.greaterThanOrEqual=" + UPDATED_TESTNUM);
    }

    @Test
    @Transactional
    void getAllEtestsByTestnumIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        etestRepository.saveAndFlush(etest);

        // Get all the etestList where testnum is less than or equal to DEFAULT_TESTNUM
        defaultEtestShouldBeFound("testnum.lessThanOrEqual=" + DEFAULT_TESTNUM);

        // Get all the etestList where testnum is less than or equal to SMALLER_TESTNUM
        defaultEtestShouldNotBeFound("testnum.lessThanOrEqual=" + SMALLER_TESTNUM);
    }

    @Test
    @Transactional
    void getAllEtestsByTestnumIsLessThanSomething() throws Exception {
        // Initialize the database
        etestRepository.saveAndFlush(etest);

        // Get all the etestList where testnum is less than DEFAULT_TESTNUM
        defaultEtestShouldNotBeFound("testnum.lessThan=" + DEFAULT_TESTNUM);

        // Get all the etestList where testnum is less than UPDATED_TESTNUM
        defaultEtestShouldBeFound("testnum.lessThan=" + UPDATED_TESTNUM);
    }

    @Test
    @Transactional
    void getAllEtestsByTestnumIsGreaterThanSomething() throws Exception {
        // Initialize the database
        etestRepository.saveAndFlush(etest);

        // Get all the etestList where testnum is greater than DEFAULT_TESTNUM
        defaultEtestShouldNotBeFound("testnum.greaterThan=" + DEFAULT_TESTNUM);

        // Get all the etestList where testnum is greater than SMALLER_TESTNUM
        defaultEtestShouldBeFound("testnum.greaterThan=" + SMALLER_TESTNUM);
    }

    @Test
    @Transactional
    void getAllEtestsByTestaddressIsEqualToSomething() throws Exception {
        // Initialize the database
        etestRepository.saveAndFlush(etest);

        // Get all the etestList where testaddress equals to DEFAULT_TESTADDRESS
        defaultEtestShouldBeFound("testaddress.equals=" + DEFAULT_TESTADDRESS);

        // Get all the etestList where testaddress equals to UPDATED_TESTADDRESS
        defaultEtestShouldNotBeFound("testaddress.equals=" + UPDATED_TESTADDRESS);
    }

    @Test
    @Transactional
    void getAllEtestsByTestaddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        etestRepository.saveAndFlush(etest);

        // Get all the etestList where testaddress not equals to DEFAULT_TESTADDRESS
        defaultEtestShouldNotBeFound("testaddress.notEquals=" + DEFAULT_TESTADDRESS);

        // Get all the etestList where testaddress not equals to UPDATED_TESTADDRESS
        defaultEtestShouldBeFound("testaddress.notEquals=" + UPDATED_TESTADDRESS);
    }

    @Test
    @Transactional
    void getAllEtestsByTestaddressIsInShouldWork() throws Exception {
        // Initialize the database
        etestRepository.saveAndFlush(etest);

        // Get all the etestList where testaddress in DEFAULT_TESTADDRESS or UPDATED_TESTADDRESS
        defaultEtestShouldBeFound("testaddress.in=" + DEFAULT_TESTADDRESS + "," + UPDATED_TESTADDRESS);

        // Get all the etestList where testaddress equals to UPDATED_TESTADDRESS
        defaultEtestShouldNotBeFound("testaddress.in=" + UPDATED_TESTADDRESS);
    }

    @Test
    @Transactional
    void getAllEtestsByTestaddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        etestRepository.saveAndFlush(etest);

        // Get all the etestList where testaddress is not null
        defaultEtestShouldBeFound("testaddress.specified=true");

        // Get all the etestList where testaddress is null
        defaultEtestShouldNotBeFound("testaddress.specified=false");
    }

    @Test
    @Transactional
    void getAllEtestsByTestaddressContainsSomething() throws Exception {
        // Initialize the database
        etestRepository.saveAndFlush(etest);

        // Get all the etestList where testaddress contains DEFAULT_TESTADDRESS
        defaultEtestShouldBeFound("testaddress.contains=" + DEFAULT_TESTADDRESS);

        // Get all the etestList where testaddress contains UPDATED_TESTADDRESS
        defaultEtestShouldNotBeFound("testaddress.contains=" + UPDATED_TESTADDRESS);
    }

    @Test
    @Transactional
    void getAllEtestsByTestaddressNotContainsSomething() throws Exception {
        // Initialize the database
        etestRepository.saveAndFlush(etest);

        // Get all the etestList where testaddress does not contain DEFAULT_TESTADDRESS
        defaultEtestShouldNotBeFound("testaddress.doesNotContain=" + DEFAULT_TESTADDRESS);

        // Get all the etestList where testaddress does not contain UPDATED_TESTADDRESS
        defaultEtestShouldBeFound("testaddress.doesNotContain=" + UPDATED_TESTADDRESS);
    }

    @Test
    @Transactional
    void getAllEtestsByTestphoneIsEqualToSomething() throws Exception {
        // Initialize the database
        etestRepository.saveAndFlush(etest);

        // Get all the etestList where testphone equals to DEFAULT_TESTPHONE
        defaultEtestShouldBeFound("testphone.equals=" + DEFAULT_TESTPHONE);

        // Get all the etestList where testphone equals to UPDATED_TESTPHONE
        defaultEtestShouldNotBeFound("testphone.equals=" + UPDATED_TESTPHONE);
    }

    @Test
    @Transactional
    void getAllEtestsByTestphoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        etestRepository.saveAndFlush(etest);

        // Get all the etestList where testphone not equals to DEFAULT_TESTPHONE
        defaultEtestShouldNotBeFound("testphone.notEquals=" + DEFAULT_TESTPHONE);

        // Get all the etestList where testphone not equals to UPDATED_TESTPHONE
        defaultEtestShouldBeFound("testphone.notEquals=" + UPDATED_TESTPHONE);
    }

    @Test
    @Transactional
    void getAllEtestsByTestphoneIsInShouldWork() throws Exception {
        // Initialize the database
        etestRepository.saveAndFlush(etest);

        // Get all the etestList where testphone in DEFAULT_TESTPHONE or UPDATED_TESTPHONE
        defaultEtestShouldBeFound("testphone.in=" + DEFAULT_TESTPHONE + "," + UPDATED_TESTPHONE);

        // Get all the etestList where testphone equals to UPDATED_TESTPHONE
        defaultEtestShouldNotBeFound("testphone.in=" + UPDATED_TESTPHONE);
    }

    @Test
    @Transactional
    void getAllEtestsByTestphoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        etestRepository.saveAndFlush(etest);

        // Get all the etestList where testphone is not null
        defaultEtestShouldBeFound("testphone.specified=true");

        // Get all the etestList where testphone is null
        defaultEtestShouldNotBeFound("testphone.specified=false");
    }

    @Test
    @Transactional
    void getAllEtestsByTestphoneContainsSomething() throws Exception {
        // Initialize the database
        etestRepository.saveAndFlush(etest);

        // Get all the etestList where testphone contains DEFAULT_TESTPHONE
        defaultEtestShouldBeFound("testphone.contains=" + DEFAULT_TESTPHONE);

        // Get all the etestList where testphone contains UPDATED_TESTPHONE
        defaultEtestShouldNotBeFound("testphone.contains=" + UPDATED_TESTPHONE);
    }

    @Test
    @Transactional
    void getAllEtestsByTestphoneNotContainsSomething() throws Exception {
        // Initialize the database
        etestRepository.saveAndFlush(etest);

        // Get all the etestList where testphone does not contain DEFAULT_TESTPHONE
        defaultEtestShouldNotBeFound("testphone.doesNotContain=" + DEFAULT_TESTPHONE);

        // Get all the etestList where testphone does not contain UPDATED_TESTPHONE
        defaultEtestShouldBeFound("testphone.doesNotContain=" + UPDATED_TESTPHONE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEtestShouldBeFound(String filter) throws Exception {
        restEtestMockMvc
            .perform(get("/api/etests?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(etest.getId().intValue())))
            .andExpect(jsonPath("$.[*].testname").value(hasItem(DEFAULT_TESTNAME)))
            .andExpect(jsonPath("$.[*].testnum").value(hasItem(DEFAULT_TESTNUM.intValue())))
            .andExpect(jsonPath("$.[*].testaddress").value(hasItem(DEFAULT_TESTADDRESS)))
            .andExpect(jsonPath("$.[*].testphone").value(hasItem(DEFAULT_TESTPHONE)));

        // Check, that the count call also returns 1
        restEtestMockMvc
            .perform(get("/api/etests/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEtestShouldNotBeFound(String filter) throws Exception {
        restEtestMockMvc
            .perform(get("/api/etests?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEtestMockMvc
            .perform(get("/api/etests/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEtest() throws Exception {
        // Get the etest
        restEtestMockMvc.perform(get("/api/etests/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void updateEtest() throws Exception {
        // Initialize the database
        etestRepository.saveAndFlush(etest);

        int databaseSizeBeforeUpdate = etestRepository.findAll().size();

        // Update the etest
        Etest updatedEtest = etestRepository.findById(etest.getId()).get();
        // Disconnect from session so that the updates on updatedEtest are not directly saved in db
        em.detach(updatedEtest);
        updatedEtest.testname(UPDATED_TESTNAME).testnum(UPDATED_TESTNUM).testaddress(UPDATED_TESTADDRESS).testphone(UPDATED_TESTPHONE);

        restEtestMockMvc
            .perform(
                put("/api/etests")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEtest))
            )
            .andExpect(status().isOk());

        // Validate the Etest in the database
        List<Etest> etestList = etestRepository.findAll();
        assertThat(etestList).hasSize(databaseSizeBeforeUpdate);
        Etest testEtest = etestList.get(etestList.size() - 1);
        assertThat(testEtest.getTestname()).isEqualTo(UPDATED_TESTNAME);
        assertThat(testEtest.getTestnum()).isEqualTo(UPDATED_TESTNUM);
        assertThat(testEtest.getTestaddress()).isEqualTo(UPDATED_TESTADDRESS);
        assertThat(testEtest.getTestphone()).isEqualTo(UPDATED_TESTPHONE);
    }

    @Test
    @Transactional
    void updateNonExistingEtest() throws Exception {
        int databaseSizeBeforeUpdate = etestRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEtestMockMvc
            .perform(
                put("/api/etests").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(etest))
            )
            .andExpect(status().isBadRequest());

        // Validate the Etest in the database
        List<Etest> etestList = etestRepository.findAll();
        assertThat(etestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEtestWithPatch() throws Exception {
        // Initialize the database
        etestRepository.saveAndFlush(etest);

        int databaseSizeBeforeUpdate = etestRepository.findAll().size();

        // Update the etest using partial update
        Etest partialUpdatedEtest = new Etest();
        partialUpdatedEtest.setId(etest.getId());

        partialUpdatedEtest.testname(UPDATED_TESTNAME).testphone(UPDATED_TESTPHONE);

        restEtestMockMvc
            .perform(
                patch("/api/etests")
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEtest))
            )
            .andExpect(status().isOk());

        // Validate the Etest in the database
        List<Etest> etestList = etestRepository.findAll();
        assertThat(etestList).hasSize(databaseSizeBeforeUpdate);
        Etest testEtest = etestList.get(etestList.size() - 1);
        assertThat(testEtest.getTestname()).isEqualTo(UPDATED_TESTNAME);
        assertThat(testEtest.getTestnum()).isEqualTo(DEFAULT_TESTNUM);
        assertThat(testEtest.getTestaddress()).isEqualTo(DEFAULT_TESTADDRESS);
        assertThat(testEtest.getTestphone()).isEqualTo(UPDATED_TESTPHONE);
    }

    @Test
    @Transactional
    void fullUpdateEtestWithPatch() throws Exception {
        // Initialize the database
        etestRepository.saveAndFlush(etest);

        int databaseSizeBeforeUpdate = etestRepository.findAll().size();

        // Update the etest using partial update
        Etest partialUpdatedEtest = new Etest();
        partialUpdatedEtest.setId(etest.getId());

        partialUpdatedEtest
            .testname(UPDATED_TESTNAME)
            .testnum(UPDATED_TESTNUM)
            .testaddress(UPDATED_TESTADDRESS)
            .testphone(UPDATED_TESTPHONE);

        restEtestMockMvc
            .perform(
                patch("/api/etests")
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEtest))
            )
            .andExpect(status().isOk());

        // Validate the Etest in the database
        List<Etest> etestList = etestRepository.findAll();
        assertThat(etestList).hasSize(databaseSizeBeforeUpdate);
        Etest testEtest = etestList.get(etestList.size() - 1);
        assertThat(testEtest.getTestname()).isEqualTo(UPDATED_TESTNAME);
        assertThat(testEtest.getTestnum()).isEqualTo(UPDATED_TESTNUM);
        assertThat(testEtest.getTestaddress()).isEqualTo(UPDATED_TESTADDRESS);
        assertThat(testEtest.getTestphone()).isEqualTo(UPDATED_TESTPHONE);
    }

    @Test
    @Transactional
    void partialUpdateEtestShouldThrown() throws Exception {
        // Update the etest without id should throw
        Etest partialUpdatedEtest = new Etest();

        restEtestMockMvc
            .perform(
                patch("/api/etests")
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEtest))
            )
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void deleteEtest() throws Exception {
        // Initialize the database
        etestRepository.saveAndFlush(etest);

        int databaseSizeBeforeDelete = etestRepository.findAll().size();

        // Delete the etest
        restEtestMockMvc
            .perform(delete("/api/etests/{id}", etest.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Etest> etestList = etestRepository.findAll();
        assertThat(etestList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
