<?php
namespace App\Library\ExampleApi;
use App\Library\DtoBuilder\AbstractDto;
class ExampleDtoWithTrait extends AbstractDto
{
    use TraitDto;
    /**
     * Url host
     * @var string
     */
    protected $url;
}