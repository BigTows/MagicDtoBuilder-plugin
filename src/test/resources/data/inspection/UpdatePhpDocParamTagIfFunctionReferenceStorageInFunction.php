<?php

use App\Library\DtoBuilder\DtoBuilder;
use App\Library\ExampleApi\ExampleDto;

$builder = DtoBuilder::create(ExampleDto::class);

/**
 * @param DtoBuilder $builder
 */
function callFunctionExample2(DtoBuilder $builder){
 $anotherBuilder = DtoBuilder::create(ExampleDto::class);
}


/**
 * @param DtoBuilder $builder
 */
function callFunctionExample(DtoBuilder $builder){
 $anotherBuilder = DtoBuilder::create(ExampleDto::class);
 <caret>callFunctionExample2($anotherBuilder);
}


callFunctionExample($builder);