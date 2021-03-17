package net.openobject.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import javax.persistence.EntityManager;
import net.openobject.IntegrationTest;
import net.openobject.domain.Covid;
import net.openobject.repository.CovidRepository;
import net.openobject.service.CovidQueryService;
import net.openobject.service.dto.CovidCriteria;
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
 * Integration tests for the {@link CovidResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CovidResourceIT {

    private static final Integer DEFAULT_CID = 1;
    private static final Integer UPDATED_CID = 2;
    private static final Integer SMALLER_CID = 1 - 1;

    private static final String DEFAULT_CTYPE = "AAAAAAAAAA";
    private static final String UPDATED_CTYPE = "BBBBBBBBBB";

    private static final String DEFAULT_CENTERNAME = "AAAAAAAAAA";
    private static final String UPDATED_CENTERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_COI = "AAAAAAAAAA";
    private static final String UPDATED_COI = "BBBBBBBBBB";

    private static final String DEFAULT_FACILITYNAME = "AAAAAAAAAA";
    private static final String UPDATED_FACILITYNAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_ZIPCODE = 1;
    private static final Integer UPDATED_ZIPCODE = 2;
    private static final Integer SMALLER_ZIPCODE = 1 - 1;

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    @Autowired
    private CovidRepository covidRepository;

    @Autowired
    private CovidQueryService covidQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCovidMockMvc;

    private Covid covid;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Covid createEntity(EntityManager em) {
        Covid covid = new Covid()
            .cid(DEFAULT_CID)
            .ctype(DEFAULT_CTYPE)
            .centername(DEFAULT_CENTERNAME)
            .coi(DEFAULT_COI)
            .facilityname(DEFAULT_FACILITYNAME)
            .zipcode(DEFAULT_ZIPCODE)
            .address(DEFAULT_ADDRESS);
        return covid;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Covid createUpdatedEntity(EntityManager em) {
        Covid covid = new Covid()
            .cid(UPDATED_CID)
            .ctype(UPDATED_CTYPE)
            .centername(UPDATED_CENTERNAME)
            .coi(UPDATED_COI)
            .facilityname(UPDATED_FACILITYNAME)
            .zipcode(UPDATED_ZIPCODE)
            .address(UPDATED_ADDRESS);
        return covid;
    }

    @BeforeEach
    public void initTest() {
        covid = createEntity(em);
    }

    @Test
    @Transactional
    void createCovid() throws Exception {
        int databaseSizeBeforeCreate = covidRepository.findAll().size();
        // Create the Covid
        restCovidMockMvc
            .perform(
                post("/api/covids").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(covid))
            )
            .andExpect(status().isCreated());

        // Validate the Covid in the database
        List<Covid> covidList = covidRepository.findAll();
        assertThat(covidList).hasSize(databaseSizeBeforeCreate + 1);
        Covid testCovid = covidList.get(covidList.size() - 1);
        assertThat(testCovid.getCid()).isEqualTo(DEFAULT_CID);
        assertThat(testCovid.getCtype()).isEqualTo(DEFAULT_CTYPE);
        assertThat(testCovid.getCentername()).isEqualTo(DEFAULT_CENTERNAME);
        assertThat(testCovid.getCoi()).isEqualTo(DEFAULT_COI);
        assertThat(testCovid.getFacilityname()).isEqualTo(DEFAULT_FACILITYNAME);
        assertThat(testCovid.getZipcode()).isEqualTo(DEFAULT_ZIPCODE);
        assertThat(testCovid.getAddress()).isEqualTo(DEFAULT_ADDRESS);
    }

    @Test
    @Transactional
    void createCovidWithExistingId() throws Exception {
        // Create the Covid with an existing ID
        covid.setId(1L);

        int databaseSizeBeforeCreate = covidRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCovidMockMvc
            .perform(
                post("/api/covids").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(covid))
            )
            .andExpect(status().isBadRequest());

        // Validate the Covid in the database
        List<Covid> covidList = covidRepository.findAll();
        assertThat(covidList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCovids() throws Exception {
        // Initialize the database
        covidRepository.saveAndFlush(covid);

        // Get all the covidList
        restCovidMockMvc
            .perform(get("/api/covids?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(covid.getId().intValue())))
            .andExpect(jsonPath("$.[*].cid").value(hasItem(DEFAULT_CID)))
            .andExpect(jsonPath("$.[*].ctype").value(hasItem(DEFAULT_CTYPE)))
            .andExpect(jsonPath("$.[*].centername").value(hasItem(DEFAULT_CENTERNAME)))
            .andExpect(jsonPath("$.[*].coi").value(hasItem(DEFAULT_COI)))
            .andExpect(jsonPath("$.[*].facilityname").value(hasItem(DEFAULT_FACILITYNAME)))
            .andExpect(jsonPath("$.[*].zipcode").value(hasItem(DEFAULT_ZIPCODE)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)));
    }

    @Test
    @Transactional
    void getCovid() throws Exception {
        // Initialize the database
        covidRepository.saveAndFlush(covid);

        // Get the covid
        restCovidMockMvc
            .perform(get("/api/covids/{id}", covid.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(covid.getId().intValue()))
            .andExpect(jsonPath("$.cid").value(DEFAULT_CID))
            .andExpect(jsonPath("$.ctype").value(DEFAULT_CTYPE))
            .andExpect(jsonPath("$.centername").value(DEFAULT_CENTERNAME))
            .andExpect(jsonPath("$.coi").value(DEFAULT_COI))
            .andExpect(jsonPath("$.facilityname").value(DEFAULT_FACILITYNAME))
            .andExpect(jsonPath("$.zipcode").value(DEFAULT_ZIPCODE))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS));
    }

    @Test
    @Transactional
    void getCovidsByIdFiltering() throws Exception {
        // Initialize the database
        covidRepository.saveAndFlush(covid);

        Long id = covid.getId();

        defaultCovidShouldBeFound("id.equals=" + id);
        defaultCovidShouldNotBeFound("id.notEquals=" + id);

        defaultCovidShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCovidShouldNotBeFound("id.greaterThan=" + id);

        defaultCovidShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCovidShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCovidsByCidIsEqualToSomething() throws Exception {
        // Initialize the database
        covidRepository.saveAndFlush(covid);

        // Get all the covidList where cid equals to DEFAULT_CID
        defaultCovidShouldBeFound("cid.equals=" + DEFAULT_CID);

        // Get all the covidList where cid equals to UPDATED_CID
        defaultCovidShouldNotBeFound("cid.equals=" + UPDATED_CID);
    }

    @Test
    @Transactional
    void getAllCovidsByCidIsNotEqualToSomething() throws Exception {
        // Initialize the database
        covidRepository.saveAndFlush(covid);

        // Get all the covidList where cid not equals to DEFAULT_CID
        defaultCovidShouldNotBeFound("cid.notEquals=" + DEFAULT_CID);

        // Get all the covidList where cid not equals to UPDATED_CID
        defaultCovidShouldBeFound("cid.notEquals=" + UPDATED_CID);
    }

    @Test
    @Transactional
    void getAllCovidsByCidIsInShouldWork() throws Exception {
        // Initialize the database
        covidRepository.saveAndFlush(covid);

        // Get all the covidList where cid in DEFAULT_CID or UPDATED_CID
        defaultCovidShouldBeFound("cid.in=" + DEFAULT_CID + "," + UPDATED_CID);

        // Get all the covidList where cid equals to UPDATED_CID
        defaultCovidShouldNotBeFound("cid.in=" + UPDATED_CID);
    }

    @Test
    @Transactional
    void getAllCovidsByCidIsNullOrNotNull() throws Exception {
        // Initialize the database
        covidRepository.saveAndFlush(covid);

        // Get all the covidList where cid is not null
        defaultCovidShouldBeFound("cid.specified=true");

        // Get all the covidList where cid is null
        defaultCovidShouldNotBeFound("cid.specified=false");
    }

    @Test
    @Transactional
    void getAllCovidsByCidIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        covidRepository.saveAndFlush(covid);

        // Get all the covidList where cid is greater than or equal to DEFAULT_CID
        defaultCovidShouldBeFound("cid.greaterThanOrEqual=" + DEFAULT_CID);

        // Get all the covidList where cid is greater than or equal to UPDATED_CID
        defaultCovidShouldNotBeFound("cid.greaterThanOrEqual=" + UPDATED_CID);
    }

    @Test
    @Transactional
    void getAllCovidsByCidIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        covidRepository.saveAndFlush(covid);

        // Get all the covidList where cid is less than or equal to DEFAULT_CID
        defaultCovidShouldBeFound("cid.lessThanOrEqual=" + DEFAULT_CID);

        // Get all the covidList where cid is less than or equal to SMALLER_CID
        defaultCovidShouldNotBeFound("cid.lessThanOrEqual=" + SMALLER_CID);
    }

    @Test
    @Transactional
    void getAllCovidsByCidIsLessThanSomething() throws Exception {
        // Initialize the database
        covidRepository.saveAndFlush(covid);

        // Get all the covidList where cid is less than DEFAULT_CID
        defaultCovidShouldNotBeFound("cid.lessThan=" + DEFAULT_CID);

        // Get all the covidList where cid is less than UPDATED_CID
        defaultCovidShouldBeFound("cid.lessThan=" + UPDATED_CID);
    }

    @Test
    @Transactional
    void getAllCovidsByCidIsGreaterThanSomething() throws Exception {
        // Initialize the database
        covidRepository.saveAndFlush(covid);

        // Get all the covidList where cid is greater than DEFAULT_CID
        defaultCovidShouldNotBeFound("cid.greaterThan=" + DEFAULT_CID);

        // Get all the covidList where cid is greater than SMALLER_CID
        defaultCovidShouldBeFound("cid.greaterThan=" + SMALLER_CID);
    }

    @Test
    @Transactional
    void getAllCovidsByCtypeIsEqualToSomething() throws Exception {
        // Initialize the database
        covidRepository.saveAndFlush(covid);

        // Get all the covidList where ctype equals to DEFAULT_CTYPE
        defaultCovidShouldBeFound("ctype.equals=" + DEFAULT_CTYPE);

        // Get all the covidList where ctype equals to UPDATED_CTYPE
        defaultCovidShouldNotBeFound("ctype.equals=" + UPDATED_CTYPE);
    }

    @Test
    @Transactional
    void getAllCovidsByCtypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        covidRepository.saveAndFlush(covid);

        // Get all the covidList where ctype not equals to DEFAULT_CTYPE
        defaultCovidShouldNotBeFound("ctype.notEquals=" + DEFAULT_CTYPE);

        // Get all the covidList where ctype not equals to UPDATED_CTYPE
        defaultCovidShouldBeFound("ctype.notEquals=" + UPDATED_CTYPE);
    }

    @Test
    @Transactional
    void getAllCovidsByCtypeIsInShouldWork() throws Exception {
        // Initialize the database
        covidRepository.saveAndFlush(covid);

        // Get all the covidList where ctype in DEFAULT_CTYPE or UPDATED_CTYPE
        defaultCovidShouldBeFound("ctype.in=" + DEFAULT_CTYPE + "," + UPDATED_CTYPE);

        // Get all the covidList where ctype equals to UPDATED_CTYPE
        defaultCovidShouldNotBeFound("ctype.in=" + UPDATED_CTYPE);
    }

    @Test
    @Transactional
    void getAllCovidsByCtypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        covidRepository.saveAndFlush(covid);

        // Get all the covidList where ctype is not null
        defaultCovidShouldBeFound("ctype.specified=true");

        // Get all the covidList where ctype is null
        defaultCovidShouldNotBeFound("ctype.specified=false");
    }

    @Test
    @Transactional
    void getAllCovidsByCtypeContainsSomething() throws Exception {
        // Initialize the database
        covidRepository.saveAndFlush(covid);

        // Get all the covidList where ctype contains DEFAULT_CTYPE
        defaultCovidShouldBeFound("ctype.contains=" + DEFAULT_CTYPE);

        // Get all the covidList where ctype contains UPDATED_CTYPE
        defaultCovidShouldNotBeFound("ctype.contains=" + UPDATED_CTYPE);
    }

    @Test
    @Transactional
    void getAllCovidsByCtypeNotContainsSomething() throws Exception {
        // Initialize the database
        covidRepository.saveAndFlush(covid);

        // Get all the covidList where ctype does not contain DEFAULT_CTYPE
        defaultCovidShouldNotBeFound("ctype.doesNotContain=" + DEFAULT_CTYPE);

        // Get all the covidList where ctype does not contain UPDATED_CTYPE
        defaultCovidShouldBeFound("ctype.doesNotContain=" + UPDATED_CTYPE);
    }

    @Test
    @Transactional
    void getAllCovidsByCenternameIsEqualToSomething() throws Exception {
        // Initialize the database
        covidRepository.saveAndFlush(covid);

        // Get all the covidList where centername equals to DEFAULT_CENTERNAME
        defaultCovidShouldBeFound("centername.equals=" + DEFAULT_CENTERNAME);

        // Get all the covidList where centername equals to UPDATED_CENTERNAME
        defaultCovidShouldNotBeFound("centername.equals=" + UPDATED_CENTERNAME);
    }

    @Test
    @Transactional
    void getAllCovidsByCenternameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        covidRepository.saveAndFlush(covid);

        // Get all the covidList where centername not equals to DEFAULT_CENTERNAME
        defaultCovidShouldNotBeFound("centername.notEquals=" + DEFAULT_CENTERNAME);

        // Get all the covidList where centername not equals to UPDATED_CENTERNAME
        defaultCovidShouldBeFound("centername.notEquals=" + UPDATED_CENTERNAME);
    }

    @Test
    @Transactional
    void getAllCovidsByCenternameIsInShouldWork() throws Exception {
        // Initialize the database
        covidRepository.saveAndFlush(covid);

        // Get all the covidList where centername in DEFAULT_CENTERNAME or UPDATED_CENTERNAME
        defaultCovidShouldBeFound("centername.in=" + DEFAULT_CENTERNAME + "," + UPDATED_CENTERNAME);

        // Get all the covidList where centername equals to UPDATED_CENTERNAME
        defaultCovidShouldNotBeFound("centername.in=" + UPDATED_CENTERNAME);
    }

    @Test
    @Transactional
    void getAllCovidsByCenternameIsNullOrNotNull() throws Exception {
        // Initialize the database
        covidRepository.saveAndFlush(covid);

        // Get all the covidList where centername is not null
        defaultCovidShouldBeFound("centername.specified=true");

        // Get all the covidList where centername is null
        defaultCovidShouldNotBeFound("centername.specified=false");
    }

    @Test
    @Transactional
    void getAllCovidsByCenternameContainsSomething() throws Exception {
        // Initialize the database
        covidRepository.saveAndFlush(covid);

        // Get all the covidList where centername contains DEFAULT_CENTERNAME
        defaultCovidShouldBeFound("centername.contains=" + DEFAULT_CENTERNAME);

        // Get all the covidList where centername contains UPDATED_CENTERNAME
        defaultCovidShouldNotBeFound("centername.contains=" + UPDATED_CENTERNAME);
    }

    @Test
    @Transactional
    void getAllCovidsByCenternameNotContainsSomething() throws Exception {
        // Initialize the database
        covidRepository.saveAndFlush(covid);

        // Get all the covidList where centername does not contain DEFAULT_CENTERNAME
        defaultCovidShouldNotBeFound("centername.doesNotContain=" + DEFAULT_CENTERNAME);

        // Get all the covidList where centername does not contain UPDATED_CENTERNAME
        defaultCovidShouldBeFound("centername.doesNotContain=" + UPDATED_CENTERNAME);
    }

    @Test
    @Transactional
    void getAllCovidsByCoiIsEqualToSomething() throws Exception {
        // Initialize the database
        covidRepository.saveAndFlush(covid);

        // Get all the covidList where coi equals to DEFAULT_COI
        defaultCovidShouldBeFound("coi.equals=" + DEFAULT_COI);

        // Get all the covidList where coi equals to UPDATED_COI
        defaultCovidShouldNotBeFound("coi.equals=" + UPDATED_COI);
    }

    @Test
    @Transactional
    void getAllCovidsByCoiIsNotEqualToSomething() throws Exception {
        // Initialize the database
        covidRepository.saveAndFlush(covid);

        // Get all the covidList where coi not equals to DEFAULT_COI
        defaultCovidShouldNotBeFound("coi.notEquals=" + DEFAULT_COI);

        // Get all the covidList where coi not equals to UPDATED_COI
        defaultCovidShouldBeFound("coi.notEquals=" + UPDATED_COI);
    }

    @Test
    @Transactional
    void getAllCovidsByCoiIsInShouldWork() throws Exception {
        // Initialize the database
        covidRepository.saveAndFlush(covid);

        // Get all the covidList where coi in DEFAULT_COI or UPDATED_COI
        defaultCovidShouldBeFound("coi.in=" + DEFAULT_COI + "," + UPDATED_COI);

        // Get all the covidList where coi equals to UPDATED_COI
        defaultCovidShouldNotBeFound("coi.in=" + UPDATED_COI);
    }

    @Test
    @Transactional
    void getAllCovidsByCoiIsNullOrNotNull() throws Exception {
        // Initialize the database
        covidRepository.saveAndFlush(covid);

        // Get all the covidList where coi is not null
        defaultCovidShouldBeFound("coi.specified=true");

        // Get all the covidList where coi is null
        defaultCovidShouldNotBeFound("coi.specified=false");
    }

    @Test
    @Transactional
    void getAllCovidsByCoiContainsSomething() throws Exception {
        // Initialize the database
        covidRepository.saveAndFlush(covid);

        // Get all the covidList where coi contains DEFAULT_COI
        defaultCovidShouldBeFound("coi.contains=" + DEFAULT_COI);

        // Get all the covidList where coi contains UPDATED_COI
        defaultCovidShouldNotBeFound("coi.contains=" + UPDATED_COI);
    }

    @Test
    @Transactional
    void getAllCovidsByCoiNotContainsSomething() throws Exception {
        // Initialize the database
        covidRepository.saveAndFlush(covid);

        // Get all the covidList where coi does not contain DEFAULT_COI
        defaultCovidShouldNotBeFound("coi.doesNotContain=" + DEFAULT_COI);

        // Get all the covidList where coi does not contain UPDATED_COI
        defaultCovidShouldBeFound("coi.doesNotContain=" + UPDATED_COI);
    }

    @Test
    @Transactional
    void getAllCovidsByFacilitynameIsEqualToSomething() throws Exception {
        // Initialize the database
        covidRepository.saveAndFlush(covid);

        // Get all the covidList where facilityname equals to DEFAULT_FACILITYNAME
        defaultCovidShouldBeFound("facilityname.equals=" + DEFAULT_FACILITYNAME);

        // Get all the covidList where facilityname equals to UPDATED_FACILITYNAME
        defaultCovidShouldNotBeFound("facilityname.equals=" + UPDATED_FACILITYNAME);
    }

    @Test
    @Transactional
    void getAllCovidsByFacilitynameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        covidRepository.saveAndFlush(covid);

        // Get all the covidList where facilityname not equals to DEFAULT_FACILITYNAME
        defaultCovidShouldNotBeFound("facilityname.notEquals=" + DEFAULT_FACILITYNAME);

        // Get all the covidList where facilityname not equals to UPDATED_FACILITYNAME
        defaultCovidShouldBeFound("facilityname.notEquals=" + UPDATED_FACILITYNAME);
    }

    @Test
    @Transactional
    void getAllCovidsByFacilitynameIsInShouldWork() throws Exception {
        // Initialize the database
        covidRepository.saveAndFlush(covid);

        // Get all the covidList where facilityname in DEFAULT_FACILITYNAME or UPDATED_FACILITYNAME
        defaultCovidShouldBeFound("facilityname.in=" + DEFAULT_FACILITYNAME + "," + UPDATED_FACILITYNAME);

        // Get all the covidList where facilityname equals to UPDATED_FACILITYNAME
        defaultCovidShouldNotBeFound("facilityname.in=" + UPDATED_FACILITYNAME);
    }

    @Test
    @Transactional
    void getAllCovidsByFacilitynameIsNullOrNotNull() throws Exception {
        // Initialize the database
        covidRepository.saveAndFlush(covid);

        // Get all the covidList where facilityname is not null
        defaultCovidShouldBeFound("facilityname.specified=true");

        // Get all the covidList where facilityname is null
        defaultCovidShouldNotBeFound("facilityname.specified=false");
    }

    @Test
    @Transactional
    void getAllCovidsByFacilitynameContainsSomething() throws Exception {
        // Initialize the database
        covidRepository.saveAndFlush(covid);

        // Get all the covidList where facilityname contains DEFAULT_FACILITYNAME
        defaultCovidShouldBeFound("facilityname.contains=" + DEFAULT_FACILITYNAME);

        // Get all the covidList where facilityname contains UPDATED_FACILITYNAME
        defaultCovidShouldNotBeFound("facilityname.contains=" + UPDATED_FACILITYNAME);
    }

    @Test
    @Transactional
    void getAllCovidsByFacilitynameNotContainsSomething() throws Exception {
        // Initialize the database
        covidRepository.saveAndFlush(covid);

        // Get all the covidList where facilityname does not contain DEFAULT_FACILITYNAME
        defaultCovidShouldNotBeFound("facilityname.doesNotContain=" + DEFAULT_FACILITYNAME);

        // Get all the covidList where facilityname does not contain UPDATED_FACILITYNAME
        defaultCovidShouldBeFound("facilityname.doesNotContain=" + UPDATED_FACILITYNAME);
    }

    @Test
    @Transactional
    void getAllCovidsByZipcodeIsEqualToSomething() throws Exception {
        // Initialize the database
        covidRepository.saveAndFlush(covid);

        // Get all the covidList where zipcode equals to DEFAULT_ZIPCODE
        defaultCovidShouldBeFound("zipcode.equals=" + DEFAULT_ZIPCODE);

        // Get all the covidList where zipcode equals to UPDATED_ZIPCODE
        defaultCovidShouldNotBeFound("zipcode.equals=" + UPDATED_ZIPCODE);
    }

    @Test
    @Transactional
    void getAllCovidsByZipcodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        covidRepository.saveAndFlush(covid);

        // Get all the covidList where zipcode not equals to DEFAULT_ZIPCODE
        defaultCovidShouldNotBeFound("zipcode.notEquals=" + DEFAULT_ZIPCODE);

        // Get all the covidList where zipcode not equals to UPDATED_ZIPCODE
        defaultCovidShouldBeFound("zipcode.notEquals=" + UPDATED_ZIPCODE);
    }

    @Test
    @Transactional
    void getAllCovidsByZipcodeIsInShouldWork() throws Exception {
        // Initialize the database
        covidRepository.saveAndFlush(covid);

        // Get all the covidList where zipcode in DEFAULT_ZIPCODE or UPDATED_ZIPCODE
        defaultCovidShouldBeFound("zipcode.in=" + DEFAULT_ZIPCODE + "," + UPDATED_ZIPCODE);

        // Get all the covidList where zipcode equals to UPDATED_ZIPCODE
        defaultCovidShouldNotBeFound("zipcode.in=" + UPDATED_ZIPCODE);
    }

    @Test
    @Transactional
    void getAllCovidsByZipcodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        covidRepository.saveAndFlush(covid);

        // Get all the covidList where zipcode is not null
        defaultCovidShouldBeFound("zipcode.specified=true");

        // Get all the covidList where zipcode is null
        defaultCovidShouldNotBeFound("zipcode.specified=false");
    }

    @Test
    @Transactional
    void getAllCovidsByZipcodeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        covidRepository.saveAndFlush(covid);

        // Get all the covidList where zipcode is greater than or equal to DEFAULT_ZIPCODE
        defaultCovidShouldBeFound("zipcode.greaterThanOrEqual=" + DEFAULT_ZIPCODE);

        // Get all the covidList where zipcode is greater than or equal to UPDATED_ZIPCODE
        defaultCovidShouldNotBeFound("zipcode.greaterThanOrEqual=" + UPDATED_ZIPCODE);
    }

    @Test
    @Transactional
    void getAllCovidsByZipcodeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        covidRepository.saveAndFlush(covid);

        // Get all the covidList where zipcode is less than or equal to DEFAULT_ZIPCODE
        defaultCovidShouldBeFound("zipcode.lessThanOrEqual=" + DEFAULT_ZIPCODE);

        // Get all the covidList where zipcode is less than or equal to SMALLER_ZIPCODE
        defaultCovidShouldNotBeFound("zipcode.lessThanOrEqual=" + SMALLER_ZIPCODE);
    }

    @Test
    @Transactional
    void getAllCovidsByZipcodeIsLessThanSomething() throws Exception {
        // Initialize the database
        covidRepository.saveAndFlush(covid);

        // Get all the covidList where zipcode is less than DEFAULT_ZIPCODE
        defaultCovidShouldNotBeFound("zipcode.lessThan=" + DEFAULT_ZIPCODE);

        // Get all the covidList where zipcode is less than UPDATED_ZIPCODE
        defaultCovidShouldBeFound("zipcode.lessThan=" + UPDATED_ZIPCODE);
    }

    @Test
    @Transactional
    void getAllCovidsByZipcodeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        covidRepository.saveAndFlush(covid);

        // Get all the covidList where zipcode is greater than DEFAULT_ZIPCODE
        defaultCovidShouldNotBeFound("zipcode.greaterThan=" + DEFAULT_ZIPCODE);

        // Get all the covidList where zipcode is greater than SMALLER_ZIPCODE
        defaultCovidShouldBeFound("zipcode.greaterThan=" + SMALLER_ZIPCODE);
    }

    @Test
    @Transactional
    void getAllCovidsByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        covidRepository.saveAndFlush(covid);

        // Get all the covidList where address equals to DEFAULT_ADDRESS
        defaultCovidShouldBeFound("address.equals=" + DEFAULT_ADDRESS);

        // Get all the covidList where address equals to UPDATED_ADDRESS
        defaultCovidShouldNotBeFound("address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllCovidsByAddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        covidRepository.saveAndFlush(covid);

        // Get all the covidList where address not equals to DEFAULT_ADDRESS
        defaultCovidShouldNotBeFound("address.notEquals=" + DEFAULT_ADDRESS);

        // Get all the covidList where address not equals to UPDATED_ADDRESS
        defaultCovidShouldBeFound("address.notEquals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllCovidsByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        covidRepository.saveAndFlush(covid);

        // Get all the covidList where address in DEFAULT_ADDRESS or UPDATED_ADDRESS
        defaultCovidShouldBeFound("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS);

        // Get all the covidList where address equals to UPDATED_ADDRESS
        defaultCovidShouldNotBeFound("address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllCovidsByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        covidRepository.saveAndFlush(covid);

        // Get all the covidList where address is not null
        defaultCovidShouldBeFound("address.specified=true");

        // Get all the covidList where address is null
        defaultCovidShouldNotBeFound("address.specified=false");
    }

    @Test
    @Transactional
    void getAllCovidsByAddressContainsSomething() throws Exception {
        // Initialize the database
        covidRepository.saveAndFlush(covid);

        // Get all the covidList where address contains DEFAULT_ADDRESS
        defaultCovidShouldBeFound("address.contains=" + DEFAULT_ADDRESS);

        // Get all the covidList where address contains UPDATED_ADDRESS
        defaultCovidShouldNotBeFound("address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllCovidsByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        covidRepository.saveAndFlush(covid);

        // Get all the covidList where address does not contain DEFAULT_ADDRESS
        defaultCovidShouldNotBeFound("address.doesNotContain=" + DEFAULT_ADDRESS);

        // Get all the covidList where address does not contain UPDATED_ADDRESS
        defaultCovidShouldBeFound("address.doesNotContain=" + UPDATED_ADDRESS);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCovidShouldBeFound(String filter) throws Exception {
        restCovidMockMvc
            .perform(get("/api/covids?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(covid.getId().intValue())))
            .andExpect(jsonPath("$.[*].cid").value(hasItem(DEFAULT_CID)))
            .andExpect(jsonPath("$.[*].ctype").value(hasItem(DEFAULT_CTYPE)))
            .andExpect(jsonPath("$.[*].centername").value(hasItem(DEFAULT_CENTERNAME)))
            .andExpect(jsonPath("$.[*].coi").value(hasItem(DEFAULT_COI)))
            .andExpect(jsonPath("$.[*].facilityname").value(hasItem(DEFAULT_FACILITYNAME)))
            .andExpect(jsonPath("$.[*].zipcode").value(hasItem(DEFAULT_ZIPCODE)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)));

        // Check, that the count call also returns 1
        restCovidMockMvc
            .perform(get("/api/covids/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCovidShouldNotBeFound(String filter) throws Exception {
        restCovidMockMvc
            .perform(get("/api/covids?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCovidMockMvc
            .perform(get("/api/covids/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCovid() throws Exception {
        // Get the covid
        restCovidMockMvc.perform(get("/api/covids/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void updateCovid() throws Exception {
        // Initialize the database
        covidRepository.saveAndFlush(covid);

        int databaseSizeBeforeUpdate = covidRepository.findAll().size();

        // Update the covid
        Covid updatedCovid = covidRepository.findById(covid.getId()).get();
        // Disconnect from session so that the updates on updatedCovid are not directly saved in db
        em.detach(updatedCovid);
        updatedCovid
            .cid(UPDATED_CID)
            .ctype(UPDATED_CTYPE)
            .centername(UPDATED_CENTERNAME)
            .coi(UPDATED_COI)
            .facilityname(UPDATED_FACILITYNAME)
            .zipcode(UPDATED_ZIPCODE)
            .address(UPDATED_ADDRESS);

        restCovidMockMvc
            .perform(
                put("/api/covids")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCovid))
            )
            .andExpect(status().isOk());

        // Validate the Covid in the database
        List<Covid> covidList = covidRepository.findAll();
        assertThat(covidList).hasSize(databaseSizeBeforeUpdate);
        Covid testCovid = covidList.get(covidList.size() - 1);
        assertThat(testCovid.getCid()).isEqualTo(UPDATED_CID);
        assertThat(testCovid.getCtype()).isEqualTo(UPDATED_CTYPE);
        assertThat(testCovid.getCentername()).isEqualTo(UPDATED_CENTERNAME);
        assertThat(testCovid.getCoi()).isEqualTo(UPDATED_COI);
        assertThat(testCovid.getFacilityname()).isEqualTo(UPDATED_FACILITYNAME);
        assertThat(testCovid.getZipcode()).isEqualTo(UPDATED_ZIPCODE);
        assertThat(testCovid.getAddress()).isEqualTo(UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void updateNonExistingCovid() throws Exception {
        int databaseSizeBeforeUpdate = covidRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCovidMockMvc
            .perform(
                put("/api/covids").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(covid))
            )
            .andExpect(status().isBadRequest());

        // Validate the Covid in the database
        List<Covid> covidList = covidRepository.findAll();
        assertThat(covidList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCovidWithPatch() throws Exception {
        // Initialize the database
        covidRepository.saveAndFlush(covid);

        int databaseSizeBeforeUpdate = covidRepository.findAll().size();

        // Update the covid using partial update
        Covid partialUpdatedCovid = new Covid();
        partialUpdatedCovid.setId(covid.getId());

        partialUpdatedCovid
            .cid(UPDATED_CID)
            .ctype(UPDATED_CTYPE)
            .coi(UPDATED_COI)
            .facilityname(UPDATED_FACILITYNAME)
            .zipcode(UPDATED_ZIPCODE);

        restCovidMockMvc
            .perform(
                patch("/api/covids")
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCovid))
            )
            .andExpect(status().isOk());

        // Validate the Covid in the database
        List<Covid> covidList = covidRepository.findAll();
        assertThat(covidList).hasSize(databaseSizeBeforeUpdate);
        Covid testCovid = covidList.get(covidList.size() - 1);
        assertThat(testCovid.getCid()).isEqualTo(UPDATED_CID);
        assertThat(testCovid.getCtype()).isEqualTo(UPDATED_CTYPE);
        assertThat(testCovid.getCentername()).isEqualTo(DEFAULT_CENTERNAME);
        assertThat(testCovid.getCoi()).isEqualTo(UPDATED_COI);
        assertThat(testCovid.getFacilityname()).isEqualTo(UPDATED_FACILITYNAME);
        assertThat(testCovid.getZipcode()).isEqualTo(UPDATED_ZIPCODE);
        assertThat(testCovid.getAddress()).isEqualTo(DEFAULT_ADDRESS);
    }

    @Test
    @Transactional
    void fullUpdateCovidWithPatch() throws Exception {
        // Initialize the database
        covidRepository.saveAndFlush(covid);

        int databaseSizeBeforeUpdate = covidRepository.findAll().size();

        // Update the covid using partial update
        Covid partialUpdatedCovid = new Covid();
        partialUpdatedCovid.setId(covid.getId());

        partialUpdatedCovid
            .cid(UPDATED_CID)
            .ctype(UPDATED_CTYPE)
            .centername(UPDATED_CENTERNAME)
            .coi(UPDATED_COI)
            .facilityname(UPDATED_FACILITYNAME)
            .zipcode(UPDATED_ZIPCODE)
            .address(UPDATED_ADDRESS);

        restCovidMockMvc
            .perform(
                patch("/api/covids")
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCovid))
            )
            .andExpect(status().isOk());

        // Validate the Covid in the database
        List<Covid> covidList = covidRepository.findAll();
        assertThat(covidList).hasSize(databaseSizeBeforeUpdate);
        Covid testCovid = covidList.get(covidList.size() - 1);
        assertThat(testCovid.getCid()).isEqualTo(UPDATED_CID);
        assertThat(testCovid.getCtype()).isEqualTo(UPDATED_CTYPE);
        assertThat(testCovid.getCentername()).isEqualTo(UPDATED_CENTERNAME);
        assertThat(testCovid.getCoi()).isEqualTo(UPDATED_COI);
        assertThat(testCovid.getFacilityname()).isEqualTo(UPDATED_FACILITYNAME);
        assertThat(testCovid.getZipcode()).isEqualTo(UPDATED_ZIPCODE);
        assertThat(testCovid.getAddress()).isEqualTo(UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void partialUpdateCovidShouldThrown() throws Exception {
        // Update the covid without id should throw
        Covid partialUpdatedCovid = new Covid();

        restCovidMockMvc
            .perform(
                patch("/api/covids")
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCovid))
            )
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void deleteCovid() throws Exception {
        // Initialize the database
        covidRepository.saveAndFlush(covid);

        int databaseSizeBeforeDelete = covidRepository.findAll().size();

        // Delete the covid
        restCovidMockMvc
            .perform(delete("/api/covids/{id}", covid.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Covid> covidList = covidRepository.findAll();
        assertThat(covidList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
