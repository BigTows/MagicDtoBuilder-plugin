# Project status 

| Branch | Travis                                                                                                                                        | Coveralls                                                                                                                                                                             |   |
|--------|-----------------------------------------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---|
| master |[![Build Status](https://travis-ci.org/BigTows/MagicDtoBuilder-plugin.svg?branch=master)](https://travis-ci.org/BigTows/MagicDtoBuilder-plugin)| [![Coverage Status](https://coveralls.io/repos/github/BigTows/MagicDtoBuilder-plugin/badge.svg?branch=dev)](https://coveralls.io/github/BigTows/MagicDtoBuilder-plugin?branch=master) |   |
| dev    |[![Build Status](https://travis-ci.org/BigTows/MagicDtoBuilder-plugin.svg?branch=dev)](https://travis-ci.org/BigTows/MagicDtoBuilder-plugin)   | [![Coverage Status](https://coveralls.io/repos/github/BigTows/MagicDtoBuilder-plugin/badge.svg?branch=dev)](https://coveralls.io/github/BigTows/MagicDtoBuilder-plugin?branch=dev)    |   |


---


For example we have, DTO:
```php
    class ExampleDto {
       /**
        * URL for connection
        * @var string 
        */
        private $url;
        
       /**
        * Data for request
        * @var array
        */
        private $data;
    }
```

Attention this dto without method

```php
AbstractDtoBuilder::create(ExampleDto::class)->
```
