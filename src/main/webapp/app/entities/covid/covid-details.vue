<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="covid">
        <h2 class="jh-entity-heading" data-cy="covidDetailsHeading"><span>Covid</span> {{ covid.id }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Cid</span>
          </dt>
          <dd>
            <span>{{ covid.cid }}</span>
          </dd>
          <dt>
            <span>Ctype</span>
          </dt>
          <dd>
            <span>{{ covid.ctype }}</span>
          </dd>
          <dt>
            <span>Centername</span>
          </dt>
          <dd>
            <span>{{ covid.centername }}</span>
          </dd>
          <dt>
            <span>Coi</span>
          </dt>
          <dd>
            <span>{{ covid.coi }}</span>
          </dd>
          <dt>
            <span>Facilityname</span>
          </dt>
          <dd>
            <span>{{ covid.facilityname }}</span>
          </dd>
          <dt>
            <span>Zipcode</span>
          </dt>
          <dd>
            <span>{{ covid.zipcode }}</span>
          </dd>
          <dt>
            <span>Address</span>
          </dt>
          <dd>
            <span>{{ covid.address }}</span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span> Back</span>
        </button>
        <router-link v-if="covid.id" :to="{ name: 'CovidEdit', params: { covidId: covid.id } }" tag="button" class="btn btn-primary">
          <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span> Edit</span>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./covid-details.component.ts"></script>
