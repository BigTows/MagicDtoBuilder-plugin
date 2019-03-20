<?php
namespace App\Library\ExampleApi;
class ExampleDtoWithTrait
{
    use TraitDto;
    /**
     * Url host
     * @var string
     */
    protected $url;
}