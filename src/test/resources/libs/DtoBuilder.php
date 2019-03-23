<?php
namespace App\Library\DtoBuilder;
class DtoBuilder
{
    private function __construct($classNameOfDto)
    {

    }

    public static final function create(string $classNameOfDto): DtoBuilder
    {
        return new DtoBuilder($classNameOfDto);
    }

    public final function build(): AbstractDto
    {

    }
}