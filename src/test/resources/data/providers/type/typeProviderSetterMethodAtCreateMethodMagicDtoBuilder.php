<?php
use App\Library\DtoBuilder\DtoBuilder;
use App\Library\ExampleApi\ExampleDto;

DtoBuilder::create(ExampleDto::class)->set<caret>Url();


