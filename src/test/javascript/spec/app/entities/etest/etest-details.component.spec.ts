/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import EtestDetailComponent from '@/entities/etest/etest-details.vue';
import EtestClass from '@/entities/etest/etest-details.component';
import EtestService from '@/entities/etest/etest.service';
import router from '@/router';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('Etest Management Detail Component', () => {
    let wrapper: Wrapper<EtestClass>;
    let comp: EtestClass;
    let etestServiceStub: SinonStubbedInstance<EtestService>;

    beforeEach(() => {
      etestServiceStub = sinon.createStubInstance<EtestService>(EtestService);

      wrapper = shallowMount<EtestClass>(EtestDetailComponent, {
        store,
        localVue,
        router,
        provide: { etestService: () => etestServiceStub },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundEtest = { id: 123 };
        etestServiceStub.find.resolves(foundEtest);

        // WHEN
        comp.retrieveEtest(123);
        await comp.$nextTick();

        // THEN
        expect(comp.etest).toBe(foundEtest);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundEtest = { id: 123 };
        etestServiceStub.find.resolves(foundEtest);

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
