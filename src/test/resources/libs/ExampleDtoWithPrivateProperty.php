<?php
namespace App\Library\ExampleApi;
use App\Library\DtoBuilder\AbstractDto;
class ExampleDtoWithPrivateProperty extends AbstractDto
{
    /**
     * Url host
     * @var string
     */
    private $url;
}