/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';

import * as config from '@/shared/config/config';
import EtestUpdateComponent from '@/entities/etest/etest-update.vue';
import EtestClass from '@/entities/etest/etest-update.component';
import EtestService from '@/entities/etest/etest.service';

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
  describe('Etest Management Update Component', () => {
    let wrapper: Wrapper<EtestClass>;
    let comp: EtestClass;
    let etestServiceStub: SinonStubbedInstance<EtestService>;

    beforeEach(() => {
      etestServiceStub = sinon.createStubInstance<EtestService>(EtestService);

      wrapper = shallowMount<EtestClass>(EtestUpdateComponent, {
        store,
        localVue,
        router,
        provide: {
          etestService: () => etestServiceStub,
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.etest = entity;
        etestServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(etestServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.etest = entity;
        etestServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(etestServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundEtest = { id: 123 };
        etestServiceStub.find.resolves(foundEtest);
        etestServiceStub.retrieve.resolves([foundEtest]);

        // WHEN
        comp.beforeRouteEnter({ params: { etestId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.etest).toBe(foundEtest);
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
