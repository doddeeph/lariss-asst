import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Description from './description';
import DescriptionDetail from './description-detail';
import DescriptionUpdate from './description-update';
import DescriptionDeleteDialog from './description-delete-dialog';

const DescriptionRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Description />} />
    <Route path="new" element={<DescriptionUpdate />} />
    <Route path=":id">
      <Route index element={<DescriptionDetail />} />
      <Route path="edit" element={<DescriptionUpdate />} />
      <Route path="delete" element={<DescriptionDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default DescriptionRoutes;
