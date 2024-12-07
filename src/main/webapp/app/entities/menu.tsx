import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';
const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/category">
        <Translate contentKey="global.menu.entities.category" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/product">
        <Translate contentKey="global.menu.entities.product" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/product-details">
        <Translate contentKey="global.menu.entities.productDetails" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/color">
        <Translate contentKey="global.menu.entities.color" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/processor">
        <Translate contentKey="global.menu.entities.processor" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/memory">
        <Translate contentKey="global.menu.entities.memory" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/storage">
        <Translate contentKey="global.menu.entities.storage" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/description">
        <Translate contentKey="global.menu.entities.description" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/screen">
        <Translate contentKey="global.menu.entities.screen" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/connectivity">
        <Translate contentKey="global.menu.entities.connectivity" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/material">
        <Translate contentKey="global.menu.entities.material" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/case-size">
        <Translate contentKey="global.menu.entities.caseSize" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/strap-color">
        <Translate contentKey="global.menu.entities.strapColor" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/strap-size">
        <Translate contentKey="global.menu.entities.strapSize" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
