/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import CovidDetailComponent from '@/entities/covid/covid-details.vue';
import CovidClass from '@/entities/covid/covid-details.component';
import CovidService from '@/entities/covid/covid.service';
import router from '@/router';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('Covid Management Detail Component', () => {
    let wrapper: Wrapper<CovidClass>;
    let comp: CovidClass;
    let covidServiceStub: SinonStubbedInstance<CovidService>;

    beforeEach(() => {
      covidServiceStub = sinon.createStubInstance<CovidService>(CovidService);

      wrapper = shallowMount<CovidClass>(CovidDetailComponent, {
        store,
        localVue,
        router,
        provide: { covidService: () => covidServiceStub },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundCovid = { id: 123 };
        covidServiceStub.find.resolves(foundCovid);

        // WHEN
        comp.retrieveCovid(123);
        await comp.$nextTick();

        // THEN
        expect(comp.covid).toBe(foundCovid);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundCovid = { id: 123 };
        covidServiceStub.find.resolves(foundCovid);

        // WHEN
        comp.beforeRouteEnter({ params: { covidId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.covid).toBe(foundCovid);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        comp.previousState();
        await comp.$nextTick();

        expect(comp.$router.currentRoute.fullPath).toContain('/');
      });
    });
  });
});
