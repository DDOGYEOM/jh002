/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';

import * as config from '@/shared/config/config';
import CovidUpdateComponent from '@/entities/covid/covid-update.vue';
import CovidClass from '@/entities/covid/covid-update.component';
import CovidService from '@/entities/covid/covid.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
const router = new Router();
localVue.use(Router);
localVue.component('font-awesome-icon', {});
localVue.component('b-input-group', {});
localVue.component('b-input-group-prepend', {});
localVue.component('b-form-datepicker', {});
localVue.component('b-form-input', {});

describe('Component Tests', () => {
  describe('Covid Management Update Component', () => {
    let wrapper: Wrapper<CovidClass>;
    let comp: CovidClass;
    let covidServiceStub: SinonStubbedInstance<CovidService>;

    beforeEach(() => {
      covidServiceStub = sinon.createStubInstance<CovidService>(CovidService);

      wrapper = shallowMount<CovidClass>(CovidUpdateComponent, {
        store,
        localVue,
        router,
        provide: {
          covidService: () => covidServiceStub,
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.covid = entity;
        covidServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(covidServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.covid = entity;
        covidServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(covidServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundCovid = { id: 123 };
        covidServiceStub.find.resolves(foundCovid);
        covidServiceStub.retrieve.resolves([foundCovid]);

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
