import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Category from './category';
import Product from './product';
import ProductDetails from './product-details';
import Color from './color';
import Processor from './processor';
import Memory from './memory';
import Storage from './storage';
import Description from './description';
import Screen from './screen';
import Connectivity from './connectivity';
import Material from './material';
import CaseSize from './case-size';
import StrapColor from './strap-color';
import StrapSize from './strap-size';
import Order from './order';
import OrderItem from './order-item';
import Customer from './customer';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="category/*" element={<Category />} />
        <Route path="product/*" element={<Product />} />
        <Route path="product-details/*" element={<ProductDetails />} />
        <Route path="color/*" element={<Color />} />
        <Route path="processor/*" element={<Processor />} />
        <Route path="memory/*" element={<Memory />} />
        <Route path="storage/*" element={<Storage />} />
        <Route path="description/*" element={<Description />} />
        <Route path="screen/*" element={<Screen />} />
        <Route path="connectivity/*" element={<Connectivity />} />
        <Route path="material/*" element={<Material />} />
        <Route path="case-size/*" element={<CaseSize />} />
        <Route path="strap-color/*" element={<StrapColor />} />
        <Route path="strap-size/*" element={<StrapSize />} />
        <Route path="order/*" element={<Order />} />
        <Route path="order-item/*" element={<OrderItem />} />
        <Route path="customer/*" element={<Customer />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
