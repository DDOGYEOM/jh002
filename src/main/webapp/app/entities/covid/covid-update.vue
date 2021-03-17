<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2 id="jh002App.covid.home.createOrEditLabel" data-cy="CovidCreateUpdateHeading">Create or edit a Covid</h2>
        <div>
          <div class="form-group" v-if="covid.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="covid.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="covid-cid">Cid</label>
            <input
              type="number"
              class="form-control"
              name="cid"
              id="covid-cid"
              data-cy="cid"
              :class="{ valid: !$v.covid.cid.$invalid, invalid: $v.covid.cid.$invalid }"
              v-model.number="$v.covid.cid.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="covid-ctype">Ctype</label>
            <input
              type="text"
              class="form-control"
              name="ctype"
              id="covid-ctype"
              data-cy="ctype"
              :class="{ valid: !$v.covid.ctype.$invalid, invalid: $v.covid.ctype.$invalid }"
              v-model="$v.covid.ctype.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="covid-centername">Centername</label>
            <input
              type="text"
              class="form-control"
              name="centername"
              id="covid-centername"
              data-cy="centername"
              :class="{ valid: !$v.covid.centername.$invalid, invalid: $v.covid.centername.$invalid }"
              v-model="$v.covid.centername.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="covid-coi">Coi</label>
            <input
              type="text"
              class="form-control"
              name="coi"
              id="covid-coi"
              data-cy="coi"
              :class="{ valid: !$v.covid.coi.$invalid, invalid: $v.covid.coi.$invalid }"
              v-model="$v.covid.coi.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="covid-facilityname">Facilityname</label>
            <input
              type="text"
              class="form-control"
              name="facilityname"
              id="covid-facilityname"
              data-cy="facilityname"
              :class="{ valid: !$v.covid.facilityname.$invalid, invalid: $v.covid.facilityname.$invalid }"
              v-model="$v.covid.facilityname.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="covid-zipcode">Zipcode</label>
            <input
              type="number"
              class="form-control"
              name="zipcode"
              id="covid-zipcode"
              data-cy="zipcode"
              :class="{ valid: !$v.covid.zipcode.$invalid, invalid: $v.covid.zipcode.$invalid }"
              v-model.number="$v.covid.zipcode.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="covid-address">Address</label>
            <input
              type="text"
              class="form-control"
              name="address"
              id="covid-address"
              data-cy="address"
              :class="{ valid: !$v.covid.address.$invalid, invalid: $v.covid.address.$invalid }"
              v-model="$v.covid.address.$model"
            />
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" class="btn btn-secondary" v-on:click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span>Cancel</span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="$v.covid.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./covid-update.component.ts"></script>
