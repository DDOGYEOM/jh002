/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';

import * as config from '@/shared/config/config';
import AttachmentUpdateComponent from '@/entities/attachment/attachment-update.vue';
import AttachmentClass from '@/entities/attachment/attachment-update.component';
import AttachmentService from '@/entities/attachment/attachment.service';

import TicketService from '@/entities/ticket/ticket.service';

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
  describe('Attachment Management Update Component', () => {
    let wrapper: Wrapper<AttachmentClass>;
    let comp: AttachmentClass;
    let attachmentServiceStub: SinonStubbedInstance<AttachmentService>;

    beforeEach(() => {
      attachmentServiceStub = sinon.createStubInstance<AttachmentService>(AttachmentService);

      wrapper = shallowMount<AttachmentClass>(AttachmentUpdateComponent, {
        store,
        localVue,
        router,
        provide: {
          attachmentService: () => attachmentServiceStub,

          ticketService: () => new TicketService(),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.attachment = entity;
        attachmentServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(attachmentServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.attachment = entity;
        attachmentServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(attachmentServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundAttachment = { id: 123 };
        attachmentServiceStub.find.resolves(foundAttachment);
        attachmentServiceStub.retrieve.resolves([foundAttachment]);

        // WHEN
        comp.beforeRouteEnter({ params: { attachmentId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.attachment).toBe(foundAttachment);
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
