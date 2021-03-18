<template>
  <div>
    <h2 id="page-heading" data-cy="EtestHeading">
      <span id="etest-heading">Etests</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refresh List</span>
        </button>
        <router-link
          :to="{ name: 'EtestCreate' }"
          tag="button"
          id="jh-create-entity"
          data-cy="entityCreateButton"
          class="btn btn-primary jh-create-entity create-etest"
        >
          <font-awesome-icon icon="plus"></font-awesome-icon>
          <span> Create a new Etest </span>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && etests && etests.length === 0">
      <span>No etests found</span>
    </div>
    <div class="table-responsive" v-if="etests && etests.length > 0">
      <table class="table table-striped" aria-describedby="etests">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span>ID</span> <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('testname')">
              <span>Testname</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'testname'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('testnum')">
              <span>Testnum</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'testnum'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('testaddress')">
              <span>Testaddress</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'testaddress'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('testphone')">
              <span>Testphone</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'testphone'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="etest in etests" :key="etest.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'EtestView', params: { etestId: etest.id } }">{{ etest.id }}</router-link>
            </td>
            <td>{{ etest.testname }}</td>
            <td>{{ etest.testnum }}</td>
            <td>{{ etest.testaddress }}</td>
            <td>{{ etest.testphone }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'EtestView', params: { etestId: etest.id } }"
                  tag="button"
                  class="btn btn-info btn-sm details"
                  data-cy="entityDetailsButton"
                >
                  <font-awesome-icon icon="eye"></font-awesome-icon>
                  <span class="d-none d-md-inline">View</span>
                </router-link>
                <router-link
                  :to="{ name: 'EtestEdit', params: { etestId: etest.id } }"
                  tag="button"
                  class="btn btn-primary btn-sm edit"
                  data-cy="entityEditButton"
                >
                  <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                  <span class="d-none d-md-inline">Edit</span>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(etest)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline">Delete</span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <span slot="modal-title"
        ><span id="jh002App.etest.delete.question" data-cy="etestDeleteDialogHeading">Confirm delete operation</span></span
      >
      <div class="modal-body">
        <p id="jhi-delete-etest-heading">Are you sure you want to delete this Etest?</p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-etest"
          data-cy="entityConfirmDeleteButton"
          v-on:click="removeEtest()"
        >
          Delete
        </button>
      </div>
    </b-modal>
    <div v-show="etests && etests.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage" :change="loadPage(page)"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./etest.component.ts"></script>
