<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="etest">
        <h2 class="jh-entity-heading" data-cy="etestDetailsHeading"><span>Etest</span> {{ etest.id }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Testname</span>
          </dt>
          <dd>
            <span>{{ etest.testname }}</span>
          </dd>
          <dt>
            <span>Testnum</span>
          </dt>
          <dd>
            <span>{{ etest.testnum }}</span>
          </dd>
          <dt>
            <span>Testaddress</span>
          </dt>
          <dd>
            <span>{{ etest.testaddress }}</span>
          </dd>
          <dt>
            <span>Testphone</span>
          </dt>
          <dd>
            <span>{{ etest.testphone }}</span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span> Back</span>
        </button>
        <router-link v-if="etest.id" :to="{ name: 'EtestEdit', params: { etestId: etest.id } }" tag="button" class="btn btn-primary">
          <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span> Edit</span>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./etest-details.component.ts"></script>
