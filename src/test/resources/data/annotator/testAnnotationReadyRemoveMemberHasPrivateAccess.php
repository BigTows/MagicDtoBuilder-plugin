<?php
use App\Library\DtoBuilder\DtoBuilder;
use App\Library\ExampleApi\ExampleDtoWithPrivateProperty;

$builder = DtoBuilder::create(ExampleDtoWithPrivateProperty::class);
$builder->setUrl();