This plugin exists for work with magic DTO builder.

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
