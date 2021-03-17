<template>
  <div>
    <h2 id="page-heading" data-cy="CovidHeading">
      <span id="covid-heading">Covids</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refresh List</span>
        </button>
        <router-link
          :to="{ name: 'CovidCreate' }"
          tag="button"
          id="jh-create-entity"
          data-cy="entityCreateButton"
          class="btn btn-primary jh-create-entity create-covid"
        >
          <font-awesome-icon icon="plus"></font-awesome-icon>
          <span> Create a new Covid </span>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && covids && covids.length === 0">
      <span>No covids found</span>
    </div>
    <div class="table-responsive" v-if="covids && covids.length > 0">
      <table class="table table-striped" aria-describedby="covids">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span>ID</span> <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('cid')">
              <span>Cid</span> <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'cid'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('ctype')">
              <span>Ctype</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'ctype'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('centername')">
              <span>Centername</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'centername'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('coi')">
              <span>Coi</span> <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'coi'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('facilityname')">
              <span>Facilityname</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'facilityname'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('zipcode')">
              <span>Zipcode</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'zipcode'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('address')">
              <span>Address</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'address'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="covid in covids" :key="covid.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'CovidView', params: { covidId: covid.id } }">{{ covid.id }}</router-link>
            </td>
            <td>{{ covid.cid }}</td>
            <td>{{ covid.ctype }}</td>
            <td>{{ covid.centername }}</td>
            <td>{{ covid.coi }}</td>
            <td>{{ covid.facilityname }}</td>
            <td>{{ covid.zipcode }}</td>
            <td>{{ covid.address }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'CovidView', params: { covidId: covid.id } }"
                  tag="button"
                  class="btn btn-info btn-sm details"
                  data-cy="entityDetailsButton"
                >
                  <font-awesome-icon icon="eye"></font-awesome-icon>
                  <span class="d-none d-md-inline">View</span>
                </router-link>
                <router-link
                  :to="{ name: 'CovidEdit', params: { covidId: covid.id } }"
                  tag="button"
                  class="btn btn-primary btn-sm edit"
                  data-cy="entityEditButton"
                >
                  <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                  <span class="d-none d-md-inline">Edit</span>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(covid)"
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
        ><span id="jh002App.covid.delete.question" data-cy="covidDeleteDialogHeading">Confirm delete operation</span></span
      >
      <div class="modal-body">
        <p id="jhi-delete-covid-heading">Are you sure you want to delete this Covid?</p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-covid"
          data-cy="entityConfirmDeleteButton"
          v-on:click="removeCovid()"
        >
          Delete
        </button>
      </div>
    </b-modal>
    <div v-show="covids && covids.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage" :change="loadPage(page)"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./covid.component.ts"></script>
