<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2 id="jh002App.etest.home.createOrEditLabel" data-cy="EtestCreateUpdateHeading">Create or edit a Etest</h2>
        <div>
          <div class="form-group" v-if="etest.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="etest.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="etest-testname">Testname</label>
            <input
              type="text"
              class="form-control"
              name="testname"
              id="etest-testname"
              data-cy="testname"
              :class="{ valid: !$v.etest.testname.$invalid, invalid: $v.etest.testname.$invalid }"
              v-model="$v.etest.testname.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="etest-testnum">Testnum</label>
            <input
              type="number"
              class="form-control"
              name="testnum"
              id="etest-testnum"
              data-cy="testnum"
              :class="{ valid: !$v.etest.testnum.$invalid, invalid: $v.etest.testnum.$invalid }"
              v-model.number="$v.etest.testnum.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="etest-testaddress">Testaddress</label>
            <input
              type="text"
              class="form-control"
              name="testaddress"
              id="etest-testaddress"
              data-cy="testaddress"
              :class="{ valid: !$v.etest.testaddress.$invalid, invalid: $v.etest.testaddress.$invalid }"
              v-model="$v.etest.testaddress.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="etest-testphone">Testphone</label>
            <input
              type="text"
              class="form-control"
              name="testphone"
              id="etest-testphone"
              data-cy="testphone"
              :class="{ valid: !$v.etest.testphone.$invalid, invalid: $v.etest.testphone.$invalid }"
              v-model="$v.etest.testphone.$model"
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
            :disabled="$v.etest.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./etest-update.component.ts"></script>
