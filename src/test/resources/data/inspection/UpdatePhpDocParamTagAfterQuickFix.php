<?php

use App\Library\DtoBuilder\DtoBuilder;
use App\Library\ExampleApi\ExampleDto;

/**
 * @param \App\Library\DtoBuilder\DtoBuilder|\App\Library\ExampleApi\ExampleDto $builder
 */
function callFunctionExample(DtoBuilder $builder){

}

$builder = DtoBuilder::create(ExampleDto::class);

callFunctionExample($builder);