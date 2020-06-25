#!/bin/bash

modules_array=(template-utils-core template-utils-core-dto-extensions template-utils-core-test-dto template-utils-documentstructure-builder)

for index in ${!modules_array[*]}
do

  grep -RUIl '<<<<<<< HEAD' ${modules_array[$index]}/src

  if [ $? -ne 1 ]; then
      echo "${modules_array[$index]}/src contains merge conflicts. Please remove them and try again."
      exit 1;
  fi
done
