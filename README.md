# Project status 

| Branch | Travis                                                                                                                                        |   |   |
|--------|-----------------------------------------------------------------------------------------------------------------------------------------------|---|---|
| master |[![Build Status](https://travis-ci.org/BigTows/MagicDtoBuilder-plugin.svg?branch=master)](https://travis-ci.org/BigTows/MagicDtoBuilder-plugin)|   |   |
| dev    |[![Build Status](https://travis-ci.org/BigTows/MagicDtoBuilder-plugin.svg?branch=dev)](https://travis-ci.org/BigTows/MagicDtoBuilder-plugin)   |   |   |


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
