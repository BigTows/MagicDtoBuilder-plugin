<?php

use App\Library\DtoBuilder\DtoBuilder;
use App\Library\ExampleApi\ExampleDto;

/**
 * @param DtoBuilder $builder
 */
function callFunctionExample(DtoBuilder $builder){

}

$builder = DtoBuilder::create(ExampleDto::class);

<caret>callFunctionExample($builder);