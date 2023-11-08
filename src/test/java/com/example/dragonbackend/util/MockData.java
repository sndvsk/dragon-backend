package com.example.dragonbackend.util;

import com.example.dragonbackend.dto.response.ItemResponse;
import com.example.dragonbackend.dto.response.MessageResponse;
import com.example.dragonbackend.dto.response.ShopResponse;
import com.example.dragonbackend.dto.response.TaskResponse;

public class MockData {

    public static final String mockGameId = "aZnNwino";

    public static final ItemResponse[] mockShopItems = new ItemResponse[]{
            new ItemResponse("hpot", "Healing potion", 50),
            new ItemResponse("cs", "Claw Sharpening", 100),
            new ItemResponse("gas", "Gasoline", 100),
            new ItemResponse("wax", "Copper Plating", 100),
            new ItemResponse("tricks", "Book of Tricks", 100),
            new ItemResponse("wingpot", "Potion of Stronger Wings", 100),
            new ItemResponse("ch", "Claw Honing", 300),
            new ItemResponse("rf", "Rocket Fuel", 300),
            new ItemResponse("iron", "Iron Plating", 300),
            new ItemResponse("mtrix", "Book of Megatricks", 300),
            new ItemResponse("wingpotmax", "Potion of Awesome Wings", 300)
    };

    public static final TaskResponse[] mockTaskResponses = new TaskResponse[]{
            new TaskResponse("YyZ7jyxc",
                    "Help Prospero Tennison to write their biographical novel about their difficulties with a deranged house",
                    23,
                    6,
                    "Gamble",
                    null),
            new TaskResponse("VmUhcK0k",
                    "Create an advertisement campaign for Andrej Bennett to promote their clothes based business",
                    25,
                    6,
                    "Risky",
                    null),
            new TaskResponse("PdpvgOMU",
                    "Help Kentigern Seymour to transport a magic beer mug to thrift shoppe in Thornmere",
                    9,
                    6,
                    "Hmmm....",
                    null),
            new TaskResponse("OTmKn5jN",
                    "Help Beryl Simpson to sell an unordinary horse on the local market",
                    15,
                    6,
                    "Piece of cake",
                    null),
            new TaskResponse("y2DJwIfa",
                    "Help Paul Imore to clean their dog",
                    4,
                    6,
                    "Piece of cake",
                    null),
            new TaskResponse("iPHUXF63",
                    "Help Josie Scrivens to sell an unordinary weed on the local market",
                    26,
                    6,
                    "Piece of cake",
                    null),
            new TaskResponse("4N7z4rSY",
                    "Escort Iesous Preston to thrift shoppe in Masonwhyte where they can meet with their long lost pot",
                    90,
                    6,
                    "Quite likely",
                    null),
            new TaskResponse("rwit8ZEC",
                    "Escort Grigorii Treloar to thrift shoppe in Millergannon where they can meet with their long lost clothes",
                    55,
                    6,
                    "Walk in the park",
                    null),
            new TaskResponse("RInBlYCH",
                    "Help Dearbháil Dickson to reach an agreement with Amordad Griffin on the matters of disputed bucket",
                    15,
                    6,
                    "Walk in the park",
                    null),
            new TaskResponse("mcPeiIje",
                    "Steal sheep delivery to Drahomír Fletcher and share some of the profits with the people.",
                    42,
                    6,
                    "Piece of cake",
                    null)
    };

    public static final MessageResponse mockMessageResponse = new MessageResponse(true,
            3,
            15,
            15,
            0,
            2,
            "You successfully solved the mission!");

    public static final ShopResponse mockShopResponse = new ShopResponse(
            true,
            60,
            3,
            1,
            5);
}

